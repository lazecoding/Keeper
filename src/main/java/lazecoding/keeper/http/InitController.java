package lazecoding.keeper.http;

import lazecoding.keeper.constant.CacheConstants;
import lazecoding.keeper.constant.RedissonWorkerConstants;
import lazecoding.keeper.model.ResultBean;
import lazecoding.keeper.task.ServerCleanTask;
import lazecoding.keeper.util.RedissonClientUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScheduledFuture;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * InitController
 *
 * @author lazecoding
 */
@Controller
@RequestMapping("interface/init")
public class InitController {

    private final static Logger logger = LoggerFactory.getLogger(InitController.class);

    @GetMapping("do-init")
    @ResponseBody
    public ResultBean doClean() {
        ResultBean resultBean = ResultBean.getInstance();
        boolean isSuccess = false;
        String message = "";
        try {
            RedissonClient redissonClient = RedissonClientUtil.getRedissonClient();
            RScheduledExecutorService rScheduledExecutorService = redissonClient.getExecutorService(RedissonWorkerConstants.DEFAULT_EXECUTOR_SERVICE.getName());
            RBucket<String> bucket = redissonClient.getBucket(CacheConstants.SERVER_CLEAN_TASK.getName());
            String taskId = bucket.get();
            if (StringUtils.hasText(taskId)) {
                logger.info("准备清除 下线服务清除任务 taskId:[{}]", taskId);
                rScheduledExecutorService.cancelTask(taskId);
                logger.info("完成清除 下线服务清除任务 taskId:[{}]", taskId);
            }
            // 注册任务
            ServerCleanTask runnable = new ServerCleanTask();
            RScheduledFuture<?> schedule = rScheduledExecutorService.scheduleWithFixedDelay(runnable, 60 * 10, 60 * 10, TimeUnit.SECONDS);
            taskId = schedule.getTaskId();
            bucket.set(taskId);
            logger.info("注册下线服务清除任务 taskId:[{}]", taskId);
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            message = "系统异常";
        }
        resultBean.setSuccess(isSuccess);
        resultBean.setMessage(message);
        return resultBean;
    }
}

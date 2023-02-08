package lazecoding.keeper.http;

import lazecoding.keeper.model.ResultBean;
import lazecoding.keeper.task.ServerCleanTask;
import lazecoding.keeper.util.RedissonClientUtil;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
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

    @GetMapping("do-init")
    @ResponseBody
    public ResultBean doClean() {
        ResultBean resultBean = ResultBean.getInstance();
        boolean isSuccess = false;
        String message = "";
        try {
            RedissonClient redissonClient = RedissonClientUtil.getRedissonClient();
            // 1. 先清空缓存
            redissonClient.getKeys().flushdb();
            // 2. 注册定时任务
            RScheduledExecutorService rScheduledExecutorService = redissonClient.getExecutorService("Default_Executor_Service");
            ServerCleanTask runnable = new ServerCleanTask();
            rScheduledExecutorService.scheduleWithFixedDelay(runnable, 10, 10, TimeUnit.SECONDS);
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

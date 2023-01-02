package lazecoding.keeper.task;

import lazecoding.keeper.config.Config;
import lazecoding.keeper.constant.CacheConstants;
import lazecoding.keeper.plugins.eventloop.EventLoop;
import lazecoding.keeper.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * ServerKeepTask
 *
 * @author lazecoding
 */
public class ServerKeepTask extends KeeperTask {

    private final static Logger logger = LoggerFactory.getLogger(ServerKeepTask.class);

    private static ServerKeepTask serverKeepTask = new ServerKeepTask();

    /**
     * 获取 ServerKeepTask 实例
     */
    public static ServerKeepTask getInstance() {
        return serverKeepTask;
    }

    /**
     * 注册任务
     */
    public void registerTask() {
        this.setTaskName("ServerKeepTask");
        this.registerTask(CacheConstants.SERVER_KEEP.getTtl().intValue() / 2);
    }

    /**
     * 执行任务
     */
    public void doTask() {
        logger.info("Task:[{}] startDate:[{}]", this.getTaskName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
        if (!EventLoop.registered) {
            logger.info("Task:[{}] failDate:[{}]", this.getTaskName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
            this.getScheduledExecutor().shutdown();
            return;
        }
        RedisTemplate redisTemplate = BeanUtil.getBean("redisTemplate", RedisTemplate.class);
        redisTemplate.expire(CacheConstants.SERVER_KEEP.getName() + Config.uid, CacheConstants.SERVER_KEEP.getTtl(), CacheConstants.SERVER_KEEP.getTimeUnit());

        logger.info("Task:[{}] endDate:[{}]", this.getTaskName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
    }

}

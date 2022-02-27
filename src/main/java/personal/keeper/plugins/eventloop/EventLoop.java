package personal.keeper.plugins.eventloop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.keeper.config.Config;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 周期事件处理器
 *
 * @author lazecoding
 */
public class EventLoop {

    private final static Logger logger = LoggerFactory.getLogger(EventLoop.class);

    /**
     * 任务释放注册
     */
    private static boolean registered = false;

    /**
     * 私有，禁止实例化
     */
    private EventLoop() {
    }

    /**
     * 注册任务，服务启动时候调用
     */
    public static void doRegister() {
        registered = true;
        logger.info("EventLoop registered");

        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("executor-event-loop");
            thread.setDaemon(true);
            return thread;
        });
        scheduledExecutor.scheduleWithFixedDelay(EventLoop::doLoop, Long.parseLong(Config.eventLoopCycle), Long.parseLong(Config.eventLoopCycle), TimeUnit.SECONDS);
    }

    /**
     * 取消任务
     */
    public static void cancel() {
        registered = false;
        logger.info("EventLoop canceled");
    }

    /**
     * 循环推送的入口
     */
    private static void doLoop() {
        if (!registered) {
            return;
        }

        logger.info("EventLoop -"
                + " executeTime:" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()
                + " executeDate:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));

        // TODO 本地周期事件

    }

}

package lazecoding.keeper.plugins.eventloop;

import lazecoding.keeper.task.OnlineChannelTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static boolean registered = false;

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
        doLoop();
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
        // 在线用户
        OnlineChannelTask.getInstance().registerTask();

    }

}

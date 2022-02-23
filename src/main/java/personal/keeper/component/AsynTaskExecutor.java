package personal.keeper.component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务执行器
 *
 * @author lazecoding
 */
public class AsynTaskExecutor {
    /**
     * CPU 核心数量
     */
    private static final int CORE_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 私有，禁止实例化
     */
    private AsynTaskExecutor() {

    }

    /**
     * 异步任务执行器
     */
    private static final ThreadPoolExecutor ASYN_EXECUTOR = new ThreadPoolExecutor(CORE_NUM, CORE_NUM * 2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName("asyn-task-executor");
                thread.setDaemon(true);
                return thread;
            }, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 提交任务
     **/
    public static void submitTask(Runnable task) {
        ASYN_EXECUTOR.execute(task);
    }

}

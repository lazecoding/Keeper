package lazecoding.keeper.plugins.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 批量执行器
 *
 * @author lazecoding
 */
public class BatchExecutor {

    private final static Logger logger = LoggerFactory.getLogger(BatchExecutor.class);

    /**
     * 在队列尾部添加消息
     */
    public static boolean offer(BatchRequestBean batchRequestBean) {
        if (ObjectUtils.isEmpty(batchRequestBean)) {
            return false;
        }
        boolean success = queue.offer(batchRequestBean);
        if (success && state == 0) {
            synchronized(task){
                task.notifyAll();
                logger.debug("BatchExecutor Task notifyAll");
            }
        }
        return success;
    }

    /**
     * 从队列头部取出消息
     */
    public static BatchRequestBean poll(BatchRequestBean batchRequestBean) {
        Object obj = queue.poll();
        return ObjectUtils.isEmpty(obj) ? null : (BatchRequestBean) obj;
    }

    /**
     * 判断队列是否为空
     */
    public static boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * 获取队列长度
     * @return
     */
    public static int size(){
        return queue.size();
    }

    /**
     * 启动消息推送
     */
    public static void registered() {
        logger.info("BatchExecutor register.");
        // 启动消费者线程
        CONSUMER_EXECUTOR.submit(task);
        logger.info("BatchExecutor registered.");
    }

    /**
     * 消费者任务
     */
    private static final Runnable task = new Runnable() {
        @Override
        public void run() {
            // 初始化线程状态为 running
            state = 1;
            while (true) {
                // 清空
                // 需要是 while，如果没数据就 wait
                while (queue.isEmpty()) {
                    try {
                        synchronized(task){
                            // 线程进入 waiting 状态
                            state = 0;
                            logger.debug("queue is empty,waiting ... start  state:" + state);
                            this.wait();
                            // 线程进入 running 状态
                            state = 1;
                            logger.debug("queue is empty,waiting ... end   state:" + state);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        state = 1;
                        logger.error("container is empty,waiting ... exception   state:" + state);
                    }
                }
                // 如果有数据就消费
                BatchRequestBean batchRequestBean = queue.poll();
                // TODO 处理消息

                if (!ObjectUtils.isEmpty(batchRequestBean)) {
                    System.out.println(batchRequestBean.toString());
                }

            }
        }
    };

    /**
     * 消费者状态 0 waiting;1 running
     */
    private static int state = 1;

    /**
     * 消息队列
     */
    private static final Queue<BatchRequestBean> queue = new ConcurrentLinkedQueue<>();


    /**
     * 消费者
     */
    private static final ThreadPoolExecutor CONSUMER_EXECUTOR = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName("cluster-pusher-consumer");
                thread.setDaemon(true);
                return thread;
            }, new ThreadPoolExecutor.CallerRunsPolicy());
}

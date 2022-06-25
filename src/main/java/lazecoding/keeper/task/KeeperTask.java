package lazecoding.keeper.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 抽象任务类
 *
 * @author lazecoding
 */
public abstract class KeeperTask {

    ObjectMapper MAPPER = new ObjectMapper();

    private final static Logger logger = LoggerFactory.getLogger(KeeperTask.class);

    private String taskName = "";

    private ScheduledExecutorService scheduledExecutor;

    public String getTaskName() {
        return taskName;
    }

    public KeeperTask(String taskName) {
        this.taskName = taskName;
    }

    public ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    public KeeperTask() {
    }

    public KeeperTask setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    /**
     * 注册周期任务，默认 15s 执行一次
     */
    public void registerTask() {
        registerTask(15);
    }

    /**
     * 注册周期任务
     *
     * @param delay 周期，单位/s
     */
    public void registerTask(int delay) {
        if (delay <= 0) {
            return;
        }
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(taskName);
            thread.setDaemon(true);
            return thread;
        });
        scheduledExecutor.scheduleWithFixedDelay(this::doTask, delay, delay, TimeUnit.SECONDS);
        logger.info("Task:[{}] registeredDate:[{}]", taskName, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
    }

    public abstract void doTask();

}


package lazecoding.keeper.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import lazecoding.keeper.component.GroupContainer;
import lazecoding.keeper.component.MessageSender;
import lazecoding.keeper.constant.ResponseCode;
import lazecoding.keeper.constant.ServerConstants;
import lazecoding.keeper.model.WebSocketResult;
import lazecoding.keeper.plugins.eventloop.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * OnlineChannelTask
 *
 * @author lazecoding
 */
public class OnlineChannelTask extends KeeperTask {

    private final static Logger logger = LoggerFactory.getLogger(OnlineChannelTask.class);

    private static OnlineChannelTask onlineChannelTask = new OnlineChannelTask();

    /**
     * 获取 OnlineChannelTask 实例
     */
    public static OnlineChannelTask getInstance() {
        return onlineChannelTask;
    }

    /**
     * 注册任务
     */
    public void registerTask() {
        this.setTaskName("OnlineChannelTask");
        this.registerTask(10);
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
        int num = GroupContainer.CHANNEL_CONTEXT.size();
        WebSocketResult webSocketResult = new WebSocketResult(ServerConstants.APP, ResponseCode.LOCAL_ONLINE_NUM.getCode(), num);
        String responseContent = null;
        try {
            responseContent = MAPPER.writeValueAsString(webSocketResult);
            MessageSender.sendLocalMessageForBroadcast(responseContent);
        } catch (JsonProcessingException e) {
            logger.error("Json 解析异常", e);
        }
        logger.info("Task:[{}] endDate:[{}]", this.getTaskName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
    }

}

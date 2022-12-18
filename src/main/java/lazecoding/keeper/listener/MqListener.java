package lazecoding.keeper.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lazecoding.keeper.component.MessageSender;
import lazecoding.keeper.config.Config;
import lazecoding.keeper.constant.MqConstants;
import lazecoding.keeper.model.WebSocketMqMessageBean;
import lazecoding.keeper.util.amqp.AmqpOperator;
import lazecoding.keeper.util.amqp.JsonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.util.ObjectUtils;

/**
 * MqListener
 *
 * @author lazecoding
 */
public class MqListener {

    private final static Logger logger = LoggerFactory.getLogger(MqListener.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void init() {
        handleWebSocketMessage();
    }

    private static void handleWebSocketMessage() {
        String queueName = MqConstants.WEBSOCKET_MESSAGE.getQueue() + Config.uid;
        TopicExchange exchange = new TopicExchange(MqConstants.WEBSOCKET_MESSAGE.getExchange(), true, false);
        Queue queue = new Queue(queueName, true, true, true);
        AmqpOperator amqpOperator = AmqpOperator.getInstance();
        amqpOperator.queueBinding(exchange, queue, MqConstants.WEBSOCKET_MESSAGE.getRoute());
        amqpOperator.registerListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                // 根据类型对症下药
                String contentType = message.getMessageProperties().getContentType();
                if (MessageProperties.CONTENT_TYPE_JSON.equals(contentType)) {
                    // handle json
                    String messageJson = JsonMessage.getJsonString(message);
                    WebSocketMqMessageBean messageBean = MAPPER.readValue(messageJson, WebSocketMqMessageBean.class);
                    if (ObjectUtils.isEmpty(messageBean)) {
                        return;
                    }
                    if (messageBean.getBroadcast()) {
                        // 广播
                        MessageSender.sendLocalMessageForBroadcast(messageBean.getMessage());
                    } else {
                        // 指定用户
                        MessageSender.sendLocalMessageToUser(messageBean.getUserIds(), messageBean.getMessage());
                    }
                }
            }
        }, queueName);
        logger.info("MqListener handleWebSocketMessage ready");
    }

}

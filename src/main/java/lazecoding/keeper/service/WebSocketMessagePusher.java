package lazecoding.keeper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lazecoding.keeper.constant.MqConstants;
import lazecoding.keeper.model.MessageBody;
import lazecoding.keeper.model.WebSocketMqMessageBean;
import lazecoding.keeper.model.WebSocketResult;
import lazecoding.keeper.util.amqp.AmqpOperator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * WebSocketMessagePusher
 *
 * @author lazecoding
 */
public class WebSocketMessagePusher {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 指定用户发送
     */
    public static boolean send(MessageBody messageBody, List<String> userIds) {
        if (ObjectUtils.isEmpty(messageBody) || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        AmqpOperator amqpOperator = AmqpOperator.getInstance();
        try {
            // 服务端通信提供 RPC 一般，本处跳过
            WebSocketResult webSocketResult = new WebSocketResult(messageBody.getApp(), messageBody.getEvent(), messageBody.getData(), messageBody.getTraceId());
            String objJson = MAPPER.writeValueAsString(webSocketResult);
            // 组织 WebSocketMqMessageBean
            WebSocketMqMessageBean webSocketMqMessageBean = new WebSocketMqMessageBean(objJson, userIds, Boolean.FALSE);
            amqpOperator.sendJsonMessage(MqConstants.WEBSOCKET_MESSAGE.getExchange(), MqConstants.WEBSOCKET_MESSAGE.getRoute(), webSocketMqMessageBean);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }

    /**
     * 广播发送
     */
    public static boolean sendAll(MessageBody messageBody) {
        if (ObjectUtils.isEmpty(messageBody)) {
            return false;
        }
        AmqpOperator amqpOperator = AmqpOperator.getInstance();
        try {
            // 服务端通信提供 RPC 一般，本处跳过
            WebSocketResult webSocketResult = new WebSocketResult(messageBody.getApp(), messageBody.getEvent(), messageBody.getData(), messageBody.getTraceId());
            String objJson = MAPPER.writeValueAsString(webSocketResult);
            // 组织 WebSocketMqMessageBean
            WebSocketMqMessageBean webSocketMqMessageBean = new WebSocketMqMessageBean(objJson, null, Boolean.TRUE);
            amqpOperator.sendJsonMessage(MqConstants.WEBSOCKET_MESSAGE.getExchange(), MqConstants.WEBSOCKET_MESSAGE.getRoute(), webSocketMqMessageBean);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }

}

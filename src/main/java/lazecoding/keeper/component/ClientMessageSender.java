package lazecoding.keeper.component;

import lazecoding.keeper.constant.AppsMqConstants;
import lazecoding.keeper.model.ClientMessageBean;
import lazecoding.keeper.util.amqp.AmqpOperator;
import org.springframework.util.ObjectUtils;

/**
 * 客户端消息推送
 *
 * @author lazecoding
 */
public class ClientMessageSender {

    /**
     * 客户端发送消息
     */
    public static void sendClientMessage(ClientMessageBean clientMessageBean) {
        if (ObjectUtils.isEmpty(clientMessageBean)) {
            return;
        }
        AmqpOperator amqpOperator = AmqpOperator.getInstance();
        String app = clientMessageBean.getApp();
        amqpOperator.sendJsonMessage(AppsMqConstants.exchange(), AppsMqConstants.queue(app), clientMessageBean);
    }
}

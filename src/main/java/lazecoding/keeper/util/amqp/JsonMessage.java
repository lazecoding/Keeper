package lazecoding.keeper.util.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

/**
 * JsonMessage
 *
 * @author lazecoding
 */
public class JsonMessage extends AmqpMessage {

    private final static Logger logger = LoggerFactory.getLogger(AmqpMessage.class);

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ENCODING = "utf-8";

    JsonMessage(Message message) {
        super(message);
    }

    JsonMessage(byte[] bytes) {
        super(bytes, new MessageProperties());
        this.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        this.getMessageProperties().setContentEncoding(DEFAULT_ENCODING);
        this.getMessageProperties().setContentLength(bytes.length);
    }

    public static String getJsonString(Message message) {
        String json = null;
        try {
            json = new String(message.getBody(), DEFAULT_ENCODING);
        } catch (Exception e) {
            logger.error("getJsonString error", e);
        }
        return json;
    }
}


package lazecoding.keeper.util.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * AmqpMessage
 *
 * @author lazecoding
 */
public class AmqpMessage extends Message {

    private final static Logger logger = LoggerFactory.getLogger(AmqpMessage.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final long serialVersionUID = 1L;

    AmqpMessage(Message message) {
        this(message.getBody(), message.getMessageProperties());
    }

    AmqpMessage(byte[] body, MessageProperties messageProperties) {
        super(body, messageProperties);
    }

    public static JsonMessage createJsonMessage(Object obj) {
        String value = null;
        try {
            value = MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("obj to string error", e);
            return null;
        }
        byte[] bytes = null;
        JsonMessage jsonMessage = null;
        if (StringUtils.hasText(value)) {
            try {
                bytes = value.getBytes(StandardCharsets.UTF_8);
                jsonMessage = new JsonMessage(bytes);
            } catch (Exception e) {
                logger.error("create JsonMessage error", e);
            }
        }
        return jsonMessage;
    }

    public void addProperty(String key, Object value) {
        this.getMessageProperties().setHeader(key, value);
    }


    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key) {
        return (T) this.getMessageProperties().getHeaders().get(key);
    }

}

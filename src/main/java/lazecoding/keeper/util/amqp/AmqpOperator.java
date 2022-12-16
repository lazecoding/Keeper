package lazecoding.keeper.util.amqp;

import lazecoding.keeper.util.BeanUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * AmqpOperator
 *
 * @author lazecoding
 */
public class AmqpOperator {

    private final RabbitTemplate rabbitTemplate = BeanUtil.getBean(RabbitTemplate.class);

    private final RabbitAdmin rabbitAdmin = BeanUtil.getBean(RabbitAdmin.class);

    private final ConnectionFactory connectionFactory = BeanUtil.getBean(ConnectionFactory.class);

    private final static AmqpOperator AMQP_OPERATOR = new AmqpOperator();

    /**
     * 默认批量消费一次的数量
     */
    private static final int DEFAULT_BATCH_SIZE = 100;

    /**
     * 默认消费者个数
     */
    private static final int DEFAULT_CONSUMER_NUM = 1;

    public static AmqpOperator getInstance() {
        return AMQP_OPERATOR;
    }

    public void queueBinding(AbstractExchange exchange, Queue queue, String route) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(route).noargs();
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 注册批量消费
     */
    public void registerListener(ChannelAwareBatchMessageListener batchMessageListener, String queueNames, int consumerNum, boolean enableAck) {
        if (!StringUtils.hasText(queueNames)) {
            throw new RegisterException("queueNames is nil");
        }
        if (ObjectUtils.isEmpty(batchMessageListener)) {
            throw new RegisterException("MessageListener is nil");
        }
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.addQueueNames(queueNames);
        container.setConsumerBatchEnabled(true);
        container.setBatchSize(DEFAULT_BATCH_SIZE);
        container.setMessageListener(batchMessageListener);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(consumerNum);
        if (enableAck) {
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        } else {
            container.setAcknowledgeMode(AcknowledgeMode.NONE);
        }
        container.start();
    }

    /**
     * 注册批量消费
     */
    public void registerListener(ChannelAwareBatchMessageListener batchMessageListener, String queueNames) {
        this.registerListener(batchMessageListener, queueNames, DEFAULT_CONSUMER_NUM, Boolean.FALSE);
    }

    /**
     * 注册消费
     */
    public void registerListener(ChannelAwareMessageListener messageListener, String queueNames, int consumerNum, boolean enableAck) {
        if (!StringUtils.hasText(queueNames)) {
            throw new RegisterException("queueNames is nil");
        }
        if (ObjectUtils.isEmpty(messageListener)) {
            throw new RegisterException("MessageListener is nil");
        }
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.addQueueNames(queueNames);
        container.setMessageListener(messageListener);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(consumerNum);
        if (enableAck) {
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        } else {
            container.setAcknowledgeMode(AcknowledgeMode.NONE);
        }
        container.start();
    }

    /**
     * 注册消费
     */
    public void registerListener(ChannelAwareMessageListener messageListener, String queueNames) {
        this.registerListener(messageListener, queueNames, DEFAULT_CONSUMER_NUM, Boolean.FALSE);
    }

    /**
     * 发送 JsonMessage
     */
    public void sendJsonMessage(String exchange, String routingKey, Object object) {
        if (!StringUtils.hasText(exchange) || !StringUtils.hasText(routingKey) || ObjectUtils.isEmpty(object)) {
            throw new MessageSendException("exist nil param");
        }
        JsonMessage jsonMessage = AmqpMessage.createJsonMessage(object);
        if (ObjectUtils.isEmpty(jsonMessage)) {
            throw new MessageSendException("AmqpMessage.createJsonMessage result is nil");
        }
        rabbitTemplate.convertAndSend(exchange, routingKey, jsonMessage);
    }

}

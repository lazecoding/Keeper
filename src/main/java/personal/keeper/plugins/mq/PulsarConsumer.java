package personal.keeper.plugins.mq;

import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import personal.keeper.component.AsynTaskExecutor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Pulsar Consumer
 *
 * @author lazecoding
 */
@Component("keeper.PulsarConsumer")
public class PulsarConsumer {

    private final static Logger logger = LoggerFactory.getLogger(PulsarConsumer.class);

    /**
     * Init Message Sync Consumer
     * <p>
     * https://www.cnblogs.com/Howinfun/p/15200728.html
     */
    public static void initMessageSyncConsumer() throws PulsarClientException {
        // TODO 用于处理分布式问题
        String subscriptionName = MqConstant.MESSAGE_SYNC.getSubscriptionName() + UUID.randomUUID().toString().replaceAll("-", "");
        subscriptionName = MqConstant.MESSAGE_SYNC.getSubscriptionName();

        PulsarInit.client.newConsumer()
                .topic(MqConstant.MESSAGE_SYNC.getTopicName())
                .subscriptionName(subscriptionName)
                .ackTimeout(10, TimeUnit.SECONDS)
                .subscriptionType(SubscriptionType.Shared)
                .messageListener((consumer, message) -> {
                    AsynTaskExecutor.submitTask(() -> {
                        message.getData();
                        message.getKey();
                        message.getMessageId();
                        logger.info("消费 key:" + message.getKey() + " data:" + new String(message.getData()));
                        try {
                            consumer.acknowledge(message);
                        } catch (PulsarClientException e) {
                            e.printStackTrace();
                        }
                    });

                })
                .subscribe();
    }

}

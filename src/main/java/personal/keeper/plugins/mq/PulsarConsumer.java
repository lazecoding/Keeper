package personal.keeper.plugins.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import personal.keeper.component.AsynTaskExecutor;
import personal.keeper.model.RequestModel;
import personal.keeper.plugins.cluster.ClusterManager;
import personal.keeper.plugins.cluster.ClusterMessageModel;

import java.util.List;
import java.util.Objects;
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

    private static final ObjectMapper MAPPER = new ObjectMapper();

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
                    // 消息转发到业务线程池处理，增加 MQ 吞吐量
                    String messageData = new String(message.getData());
                    logger.info("cost message:" + messageData);
                    AsynTaskExecutor.submitTask(() -> {
                        try {
                            ClusterMessageModel clusterMessageModel = MAPPER.readValue(messageData, ClusterMessageModel.class);
                            ClusterManager.consumeClusterMessage(clusterMessageModel);
                            consumer.acknowledge(message);
                        } catch (JsonProcessingException | PulsarClientException e) {
                            e.printStackTrace();
                        }
                    });

                })
                .subscribe();
    }

}

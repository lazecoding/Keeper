package lazecoding.keeper.plugins.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import lazecoding.keeper.component.AsynTaskExecutor;
import lazecoding.keeper.plugins.cluster.ClusterManager;
import lazecoding.keeper.plugins.cluster.ClusterMessageModel;

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
     */
    public static void initMessageSyncConsumer() throws PulsarClientException {
        String subscriptionName = MqConstant.MESSAGE_SYNC.getSubscriptionName();
        String topicName = MqConstant.MESSAGE_SYNC.getTopicName();
        // 初始化并启动广播订阅
        String localSubscriptionName = BroadcastComponent.initBroadcastTopic(topicName, subscriptionName);
        PulsarInit.client.newConsumer()
                .topic(topicName)
                .subscriptionName(localSubscriptionName)
                .ackTimeout(10, TimeUnit.SECONDS)
                .subscriptionType(SubscriptionType.Shared)
                .messageListener((consumer, message) -> {
                    // 消息转发到业务线程池处理，增加 MQ 吞吐量
                    String messageData = new String(message.getData());
                    logger.debug("message-sync-consumer topic:{} subscription:{} message.data:{}", topicName, localSubscriptionName, messageData);
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
        logger.info("init Message Sync Consumer ready.");
    }

}

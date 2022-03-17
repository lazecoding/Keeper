package lazecoding.keeper.plugins.cluster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import lazecoding.keeper.component.MessageSender;
import lazecoding.keeper.config.Config;
import lazecoding.keeper.constant.DigitalConstant;
import lazecoding.keeper.plugins.mq.MqConstant;
import lazecoding.keeper.plugins.mq.PulsarProducer;

import java.util.List;

/**
 * Cluster Manager
 *
 * @author lazecoding
 */
public class ClusterManager {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final static Logger logger = LoggerFactory.getLogger(ClusterManager.class);

    /**
     * 广播消息到多个服务实例
     */
    public static void sendClusterMessage(ClusterMessageModel clusterMessageModel) {
        Producer<byte[]> producer = PulsarProducer.getInstance(MqConstant.MESSAGE_SYNC.getTopicName());
        try {
            String clusterMessage = MAPPER.writeValueAsString(clusterMessageModel);
            producer.newMessage()
                    .key("cluster-message")
                    .value(clusterMessage.getBytes())
                    .sendAsync();
            logger.debug("message-sync-producer topic:{}  message.data:{}", MqConstant.MESSAGE_SYNC.getTopicName(), clusterMessageModel.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 消费来自 MQ 的消息
     *
     * @param clusterMessageModel
     */
    public static void consumeClusterMessage(ClusterMessageModel clusterMessageModel) {
        if (clusterMessageModel == null || !Config.enableCluster) {
            return;
        }
        if (clusterMessageModel.getType() == DigitalConstant.ONE) {
            // User
            List<String> userIds;
            if (CollectionUtils.isEmpty(userIds = clusterMessageModel.getUserIds())) {
                return;
            }
            MessageSender.sendLocalMessageToUser(userIds, clusterMessageModel.getMessage());
        }
        if (clusterMessageModel.getType() == DigitalConstant.TWO) {
            // Broadcast
            MessageSender.sendLocalMessageForBroadcast(clusterMessageModel.getMessage());
        }
    }

}

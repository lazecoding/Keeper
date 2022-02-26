package personal.keeper.plugins.cluster;

import org.springframework.util.CollectionUtils;
import personal.keeper.component.MessageSender;
import personal.keeper.config.Config;
import personal.keeper.constant.DigitalConstant;

import java.util.List;

/**
 * Cluster Manager
 *
 * @author lazecoding
 */
public class ClusterManager {

    /**
     * 广播消息到多个服务实例
     */
    public static void sendClusterMessage(ClusterMessageModel clusterMessageModel) {
        // TODO 通过 MQ 广播消息到多个服务实例

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

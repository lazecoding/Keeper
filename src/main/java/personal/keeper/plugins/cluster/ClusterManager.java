package personal.keeper.plugins.cluster;

import personal.keeper.component.MessageSender;
import personal.keeper.config.Config;
import personal.keeper.constant.DigitalConstant;

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
            MessageSender.sendLocalMessageToGroup(clusterMessageModel.getUserId(), clusterMessageModel.getMessage());
        }
        if (clusterMessageModel.getType() == DigitalConstant.TWO) {
            // Group
            MessageSender.sendLocalMessageToGroup(clusterMessageModel.getGroupId(), clusterMessageModel.getMessage());
        }
        if (clusterMessageModel.getType() == DigitalConstant.THREE) {
            // Broadcast
            MessageSender.sendLocalMessageForBroadcast(clusterMessageModel.getMessage());
        }
    }


}

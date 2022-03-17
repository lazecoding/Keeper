package lazecoding.keeper.component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import lazecoding.keeper.config.Config;
import lazecoding.keeper.constant.DigitalConstant;
import lazecoding.keeper.plugins.cluster.ClusterManager;
import lazecoding.keeper.plugins.cluster.ClusterMessageModel;
import lazecoding.keeper.plugins.group.GroupManager;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 信息发送器
 *
 * @author lazecoding
 */
public class MessageSender {

    /**
     * 私有，禁止实例化
     */
    private MessageSender() {
    }

    /**
     * 发送信息给本地链接
     *
     * @return
     */
    public static boolean sendLocalMessage(ChannelHandlerContext ctx, String message) {
        try {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
        } catch (Exception e) {
            // 有的时候，取出 ChannelHandlerContext 后，用户退出了，就 null 了
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 发送信息给本地用户
     */
    public static void sendLocalMessageToUser(String userId, String message) {
        if (!Config.enableUser || StringUtils.isEmpty(userId)) {
            return;
        }
        CopyOnWriteArraySet<String> channelSet = GroupContainer.USER_CHANNEL.get(userId);
        if (channelSet != null && channelSet.size() > DigitalConstant.ZERO) {
            ChannelHandlerContext ctx;
            for (String channelId : channelSet) {
                ctx = GroupContainer.CHANNEL_CONTEXT.get(channelId);
                if (!sendLocalMessage(ctx, message)) {
                    // 失败
                }
            }
        }
    }

    /**
     * 发送信息给本地用户
     */
    public static void sendLocalMessageToUser(List<String> userIds, String message) {
        if (!Config.enableUser || CollectionUtils.isEmpty(userIds)) {
            return;
        }
        for (String userId : userIds) {
            sendLocalMessageToUser(userId, message);
        }
    }

    /**
     * 发送信息给本地群组
     */
    public static void sendLocalMessageToGroup(String groupId, String message) {
        if (!Config.enableGroup || StringUtils.isEmpty(groupId)) {
            return;
        }
        List<String> userIds = GroupManager.findUserIdInGroup(groupId);
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        sendLocalMessageToUser(userIds, message);
    }

    /**
     * 本地广播
     *
     * @return
     */
    public static void sendLocalMessageForBroadcast(String message) {
        Set<String> channelSet = GroupContainer.CHANNEL_CONTEXT.keySet();
        if (channelSet.size() > DigitalConstant.ZERO) {
            ChannelHandlerContext ctx;
            for (String channelId : channelSet) {
                ctx = GroupContainer.CHANNEL_CONTEXT.get(channelId);
                if (!sendLocalMessage(ctx, message)) {
                    // 失败
                }
            }
        }
    }

    /**
     * 发送信息给集群用户
     */
    public static void sendMessageToUser(String userId, String message) {
        if (!Config.enableUser) {
            return;
        }
        if (!Config.enableCluster) {
            sendLocalMessageToUser(userId, message);
            return;
        }
        ClusterManager.sendClusterMessage(ClusterMessageModel.getUserInstance(userId, message));
    }

    /**
     * 发送信息给集群用户
     */
    public static void sendMessageToUser(List<String> userIds, String message) {
        if (!Config.enableUser || CollectionUtils.isEmpty(userIds)) {
            return;
        }
        if (!Config.enableCluster) {
            sendLocalMessageToUser(userIds, message);
            return;
        }
        ClusterManager.sendClusterMessage(ClusterMessageModel.getUserInstance(userIds, message));
    }

    /**
     * 发送信息给集群群组（转化为发给多个用户）
     */
    public static void sendMessageToGroup(String groupId, String message) {
        if (!Config.enableGroup || StringUtils.isEmpty(groupId)) {
            return;
        }
        List<String> userIds = GroupManager.findUserIdInGroup(groupId);
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        if (!Config.enableCluster) {
            // 发给本地用户
            sendLocalMessageToUser(userIds, message);
            return;
        }
        // 发给集群用户
        ClusterManager.sendClusterMessage(ClusterMessageModel.getUserInstance(userIds, message));
    }

    /**
     * 集群广播
     *
     * @return
     */
    public static void sendMessageForBroadcast(String message) {
        if (!Config.enableCluster) {
            sendLocalMessageForBroadcast(message);
            return;
        }
        ClusterManager.sendClusterMessage(ClusterMessageModel.getBroadcastInstance(message));
    }

}

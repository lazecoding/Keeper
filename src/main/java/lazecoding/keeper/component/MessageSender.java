package lazecoding.keeper.component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import lazecoding.keeper.constant.DigitalConstant;

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
    public static void sendLocalMessageToUser(String accessToken, String message) {
        if (!StringUtils.hasText(accessToken) ) {
            return;
        }
        CopyOnWriteArraySet<String> channelSet = GroupContainer.USER_CHANNEL.get(accessToken);
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
    public static void sendLocalMessageToUser(List<String> accessTokens, String message) {
        if (CollectionUtils.isEmpty(accessTokens) ) {
            return;
        }
        for (String accessToken : accessTokens) {
            sendLocalMessageToUser(accessToken, message);
        }
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


}

package lazecoding.keeper.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lazecoding.keeper.config.Config;
import lazecoding.keeper.constant.DigitalConstant;
import lazecoding.keeper.constant.ResponseCode;
import lazecoding.keeper.constant.ServerConstants;
import lazecoding.keeper.model.ResendBean;
import lazecoding.keeper.model.WebSocketResult;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 信息发送器
 *
 * @author lazecoding
 */
public class MessageSender {

    private static final ObjectMapper MAPPER = new ObjectMapper();

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
        return sendLocalMessage(ctx, message, 0);
    }

    /**
     * 发送信息给本地链接
     *
     * @return
     */
    private static boolean sendLocalMessage(ChannelHandlerContext ctx, String message, int retry) {
        try {
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
            // 开启重推且未达到重推次数上限
            if (Config.enableResend && (retry <= Config.maxResendTime || Config.maxResendTime < 0)) {
                retry++;
                ResendBean resendBean = new ResendBean(ctx, message, retry);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (!channelFuture.isSuccess()) {
                            // 延时重试
                            AsyncTaskExecutor.submitDelayTask(() -> {
                                MessageSender.sendLocalMessage(resendBean.getCtx(), resendBean.getContent(), resendBean.getRetry());
                                System.out.println("AsyncTaskExecutor.submitDelayTask ResendBean retry:" + resendBean.getRetry() + " time:" + System.currentTimeMillis() + " content:" + resendBean.getContent());
                            }, Config.resendCycle);
                        }
                    }
                });
            }
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
        if (!StringUtils.hasText(userId)) {
            return;
        }
        CopyOnWriteArraySet<String> channelSet = GroupContainer.USER_CHANNEL.get(userId);
        if (channelSet != null && channelSet.size() > DigitalConstant.ZERO) {
            channelSet.forEach((c) -> {
                ChannelHandlerContext ctx = GroupContainer.CHANNEL_CONTEXT.get(c);
                if (!sendLocalMessage(ctx, message)) {
                    // 失败
                }
            });

        }
    }

    /**
     * 发送信息给本地用户
     */
    public static void sendLocalMessageToUser(List<String> userIds, String message) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        for (String userId : userIds) {
            sendLocalMessageToUser(userId, message);
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

    /**
     * 成功响应
     */
    public static void successResponse(ChannelHandlerContext ctx, String message) {
        try {
            WebSocketResult webSocketResult = new WebSocketResult(ServerConstants.APP, ResponseCode.SUCCESS.getCode(), message);
            String responseContent = MAPPER.writeValueAsString(webSocketResult);
            MessageSender.sendLocalMessage(ctx, responseContent);
        } catch (JsonProcessingException e) {
            // do nothing
        }
    }

    /**
     * 异常响应
     */
    public static void errorResponse(ChannelHandlerContext ctx, String error) {
        try {
            WebSocketResult webSocketResult = new WebSocketResult(ServerConstants.APP, ResponseCode.EXCEPTION.getCode(), error);
            String responseContent = MAPPER.writeValueAsString(webSocketResult);
            MessageSender.sendLocalMessage(ctx, responseContent);
        } catch (JsonProcessingException e) {
            // do nothing
        }
    }

}

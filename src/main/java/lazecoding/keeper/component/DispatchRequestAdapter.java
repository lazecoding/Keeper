package lazecoding.keeper.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import lazecoding.keeper.constant.AppsMqConstants;
import lazecoding.keeper.model.ClientMessageBean;
import lazecoding.keeper.model.WebSocketRequest;
import lazecoding.keeper.util.amqp.AmqpOperator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 请求调度器
 *
 * @author lazecoding
 */
@Component
public class DispatchRequestAdapter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 请求调度
     * <p>
     * 对于耗时的业务，用业务线程池 {@link AsyncTaskExecutor} 执行,小业务当前 worker 线程池完成
     */
    public void dispatchRequest(ChannelHandlerContext ctx, WebSocketRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            MessageSender.errorResponse(ctx, "request is nil.");
            return;
        }
        String app = request.getApp();
        String event = request.getEvent();
        if (!StringUtils.hasText(app)) {
            MessageSender.errorResponse(ctx, "request.app is nil.");
            return;
        }
        if (!StringUtils.hasText(event)) {
            MessageSender.errorResponse(ctx, "request.event is nil.");
            return;
        }

        // 获取当前链接的 userId
        String channelId = ctx.channel().id().asLongText();
        String userId = GroupContainer.CHANNEL_USER.get(channelId);
        if (!StringUtils.hasText(userId)) {
            MessageSender.errorResponse(ctx, "user is nil.");
            return;
        }
        // 组织请求的更多属性
        ClientMessageBean clientMessageBean = new ClientMessageBean(app, event, request.getData(), userId);
        try {
            ClientMessageSender.sendClientMessage(clientMessageBean);
            MessageSender.successResponse(ctx, "request send to app.");
        } catch (Exception e) {
            MessageSender.errorResponse(ctx, "request send to app exception.");
        }
    }
}

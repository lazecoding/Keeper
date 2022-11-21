package lazecoding.keeper.hander;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lazecoding.keeper.component.DispatchRequestAdapter;
import lazecoding.keeper.component.MessageSender;
import lazecoding.keeper.model.WebSocketRequest;
import lazecoding.keeper.util.BeanUtil;
import org.springframework.util.StringUtils;

/**
 * 入站请求处理器
 *
 * @author lazecoding
 */
@ChannelHandler.Sharable
public class IncomingRequestHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * PING 请求消息内容
     */
    private static final String PING = "I";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String requestContext = frame.text();
        WebSocketRequest webSocketRequest;

        if (!StringUtils.hasText(requestContext)) {
            MessageSender.errorResponse(ctx, "Nil Message");
            return;
        }

        // 心跳
        if (PING.equals(requestContext)) {
            return;
        }

        try {
            webSocketRequest = MAPPER.readValue(requestContext, WebSocketRequest.class);
            incomingRequest(ctx, webSocketRequest);
        } catch (JsonProcessingException e) {
            MessageSender.errorResponse(ctx, "IncomingRequestHander JsonProcessingException");
        } catch (Exception e) {
            MessageSender.errorResponse(ctx, "IncomingRequestHander Exception");
            e.printStackTrace();
        }
    }

    /**
     * 入站请求
     */
    private void incomingRequest(ChannelHandlerContext ctx, WebSocketRequest request) throws JsonProcessingException {
        // 业务请求
        DispatchRequestAdapter dispatchRequestAdapter = BeanUtil.getBean(DispatchRequestAdapter.class);
        dispatchRequestAdapter.dispatchRequest(ctx, request);
    }

}

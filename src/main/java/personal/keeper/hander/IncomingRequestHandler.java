package personal.keeper.hander;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.StringUtils;
import personal.keeper.component.DispatchRequestAdapter;
import personal.keeper.component.GroupContainer;
import personal.keeper.component.MessageSender;
import personal.keeper.constant.RequestType;
import personal.keeper.constant.ResponseType;
import personal.keeper.model.RequestModel;
import personal.keeper.model.ResponseModel;
import personal.keeper.util.BeanUtil;

/**
 * 入站请求处理器
 *
 * @author lazecoding
 */
@ChannelHandler.Sharable
public class IncomingRequestHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * PING 请求消息内容
     */
    private static final String PING = "I";

    /**
     * P0NG 响应消息内容
     */
    private static final String PONG = "0";


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String requestContext = frame.text();
        RequestModel requestModel;

        if (StringUtils.isEmpty(requestContext)){
            errorRequest(ctx, "Nil Message");
            return;
        }

        // 心跳
        if (PING.equals(requestContext)){
            MessageSender.sendLocalMessage(ctx, PONG);
            return;
        }

        try {
            requestModel = mapper.readValue(requestContext, RequestModel.class);
            incomingRequest(ctx, requestModel);
        } catch (JsonProcessingException e) {
            errorRequest(ctx, "IncomingRequestHander JsonProcessingException");
        } catch (Exception e) {
            errorRequest(ctx, "IncomingRequestHander Exception");
            e.printStackTrace();
        }
    }

    /**
     * 入站请求
     */
    private void incomingRequest(ChannelHandlerContext ctx, RequestModel requestModel) throws JsonProcessingException {
        String type = requestModel.getType();
        if (!RequestType.contain(type)) {
            ResponseModel responseModel = new ResponseModel(ResponseType.EXCEPTION.getCode(), "不合法的请求类型");
            String responseContent = mapper.writeValueAsString(responseModel);
            MessageSender.sendLocalMessage(ctx, responseContent);
            return;
        }

        if (type.equals(RequestType.MONITOR.getCode())) {
            // M
            ResponseModel responseModel = new ResponseModel(ResponseType.MONITOR.getCode(), GroupContainer.CHANNEL_CONTEXT.size() + "");
            String responseContent = mapper.writeValueAsString(responseModel);
            MessageSender.sendLocalMessage(ctx, responseContent);
            return;
        }

        // 业务请求
        DispatchRequestAdapter dispatchRequestAdapter = BeanUtil.getBean(DispatchRequestAdapter.class);
        dispatchRequestAdapter.dispatchRequest(ctx, requestModel);
    }

    /**
     * 异常请求
     */
    private void errorRequest(ChannelHandlerContext ctx, String error) throws JsonProcessingException {
        ResponseModel responseModel = new ResponseModel(ResponseType.EXCEPTION.getCode(), error);
        String responseContent = mapper.writeValueAsString(responseModel);
        MessageSender.sendLocalMessage(ctx, responseContent);
    }


}

package lazecoding.keeper.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import lazecoding.keeper.constant.RequestType;
import lazecoding.keeper.constant.ResponseType;
import lazecoding.keeper.model.RequestModel;
import lazecoding.keeper.model.ResponseModel;
import lazecoding.keeper.service.ExecuteRequestService;
import lazecoding.keeper.util.BeanUtil;

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
    public void dispatchRequest(ChannelHandlerContext ctx, RequestModel requestModel) throws JsonProcessingException {

        String requestType = requestModel.getType();
        String requestContent = requestModel.getContent();

        ExecuteRequestService executeRequestService;

        // 根据请求获取实例
        if (requestType.equals(RequestType.T_1.getCode())) {
            executeRequestService = BeanUtil.getBean("batch-test-service", ExecuteRequestService.class);
        } else {
            // 没有匹配到 ExecuteRequestService 实例
            ResponseModel responseModel = new ResponseModel(ResponseType.EXCEPTION.getCode(), "请求分发异常");
            String responseContent = MAPPER.writeValueAsString(responseModel);
            MessageSender.sendLocalMessage(ctx, responseContent);
            return;
        }

        // 异步线程池处理
        ExecuteRequestService finalExecuteRequestService = executeRequestService;
        AsyncTaskExecutor.submitTask(() -> finalExecuteRequestService.doRequest(ctx, requestContent));
    }
}

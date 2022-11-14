package lazecoding.keeper.service;

import io.netty.channel.ChannelHandlerContext;
import lazecoding.keeper.constant.RequestType;
import lazecoding.keeper.model.ResponseModel;
import lazecoding.keeper.plugins.batch.BatchRequestBean;
import lazecoding.keeper.plugins.batch.BatchExecutor;
import org.springframework.stereotype.Service;

/**
 * 批量测试消息
 *
 * @author lazecoding
 */
@Service("batch-test-service")
public class BatchTestService implements ExecuteRequestService{
    @Override
    public void doRequest(ChannelHandlerContext ctx, String requestContext) {
        ResponseModel responseModel = new ResponseModel(RequestType.T_1.getCode(), requestContext);

        BatchExecutor.offer(new BatchRequestBean("111", responseModel).addResponseModel(responseModel).addResponseModel(responseModel));
    }
}

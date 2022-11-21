package lazecoding.keeper.service;

import io.netty.channel.ChannelHandlerContext;
import lazecoding.keeper.constant.RequestType;
import lazecoding.keeper.plugins.batch.BatchResponseModel;
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
        BatchResponseModel batchResponseModel = new BatchResponseModel(RequestType.T_1.getCode(), requestContext);

        BatchExecutor.offer(new BatchRequestBean("111", batchResponseModel).addResponseModel(batchResponseModel).addResponseModel(batchResponseModel));
    }
}

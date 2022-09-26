package lazecoding.keeper.service;

import io.netty.channel.ChannelHandlerContext;
import lazecoding.keeper.constant.RequestType;
import lazecoding.keeper.model.ResponseModel;
import lazecoding.keeper.plugins.cluster.ClusterMessageBean;
import lazecoding.keeper.plugins.cluster.ClusterPusher;
import org.springframework.stereotype.Service;

/**
 * 集群测试消息
 *
 * @author lazecoding
 */
@Service("cluster-service")
public class ClusterService implements ExecuteRequestService{
    @Override
    public void doRequest(ChannelHandlerContext ctx, String requestContext) {
        ResponseModel responseModel = new ResponseModel(RequestType.T_1.getCode(), requestContext);

        ClusterPusher.offer(new ClusterMessageBean("111", responseModel).addResponseModel(responseModel).addResponseModel(responseModel));
    }
}

package lazecoding.keeper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * 执行请求接口类
 *
 * @author lazecoding
 */
public interface ExecuteRequestService {
    /**
     * jackson 序列化
     */
    ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 处理请求
     *
     * @param ctx
     * @param requestModel 请求体（json）
     */
    void doRequest(ChannelHandlerContext ctx, String requestModel);
}

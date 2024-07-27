package lazecoding.keeper.hander;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lazecoding.keeper.component.GroupContainer;
import lazecoding.keeper.config.Config;
import lazecoding.keeper.constant.DigitalConstant;
import lazecoding.keeper.constant.QueryKeys;
import lazecoding.keeper.util.ChannelUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * （默认）会话处理器
 * <p>
 * 连接请求: handlerAdded()    -> channelRegistered()   -> channelActive() -> channelRead() -> channelReadComplete()
 * 数据请求: channelRead()     -> channelReadComplete()
 * 通道关闭: channelInactive() -> channelUnregistered() -> handlerRemoved()
 *
 * @author lazecoding
 */
@ChannelHandler.Sharable
public class DefaultSessionHandler extends ChannelInboundHandlerAdapter {

    /**
     * ?
     */
    private static final String END_TAG = "?";

    /**
     * 解析 connect 的参数列表，并建立链接
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 只有在建立连接的请求，才会进入该方法
        if (msg instanceof FullHttpRequest) {
            // 转化为 http 请求
            FullHttpRequest request = (FullHttpRequest) msg;
            // 拿到请求地址
            String uri = request.uri();
            String channelId = ChannelUtil.getChannelId(ctx);
            if (!Config.contextPath.equals(uri)) {
                // 判断是不是我的 WebSocket 请求
                // 校验 WebSocket Path 后面是否跟着 ？，否则可能出现以 WebSocket Path 前缀的请求进来
                if (!uri.startsWith(Config.contextPath + END_TAG)) {
                    ctx.close();
                    return;
                }

                // 解析 uri
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(new URI(uri));
                UriComponents uriComponents = uriComponentsBuilder.build();
                // 使用方式 value = queryParams.get(key).get(0)
                MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();

                String userId;
                if (!queryParams.containsKey(QueryKeys.U.name())
                        || !StringUtils.hasText(userId = queryParams.get(QueryKeys.U.name()).get(DigitalConstant.ZERO))) {
                    ctx.close();
                    return;
                }

                // 该 channelId 和哪个 userId 绑定
                GroupContainer.CHANNEL_USER.put(channelId, userId);

                // 该 userId 绑定了哪些 channelId
                CopyOnWriteArraySet<String> channelSet = GroupContainer.USER_CHANNEL.putIfAbsent(userId, new CopyOnWriteArraySet<>());
                if (channelSet == null) {
                    channelSet = GroupContainer.USER_CHANNEL.get(userId);
                }
                channelSet.add(channelId);

                // 处理参数列表
                // 重新设置 WebSocket Path
                request.setUri(Config.contextPath);
            }
            // 维护所有链接
            GroupContainer.CHANNEL_CONTEXT.put(channelId, ctx);
        }
        // 建立请求
        super.channelRead(ctx, msg);
    }

    /**
     * 建立连接以后第一个调用的方法
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // 由 channelActive 处理
    }

    /**
     * channel连接状态就绪以后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    /**
     * channel连接状态断开后触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String channelId = ChannelUtil.getChannelId(ctx);
        GroupContainer.CHANNEL_CONTEXT.remove(channelId);

        if (GroupContainer.CHANNEL_USER.containsKey(channelId)) {
            // 删除正向关系
            String userId = GroupContainer.CHANNEL_USER.get(channelId);
            GroupContainer.CHANNEL_USER.remove(channelId);
            // 删除反向关系
            CopyOnWriteArraySet<String> channelSet = GroupContainer.USER_CHANNEL.get(userId);
            if (!CollectionUtils.isEmpty(channelSet)) {
                channelSet.remove(channelId);
            }
        }
    }

    /**
     * 连接发生异常时触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    /**
     * 断开连接会触发该消息
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        ctx.close();
    }
}

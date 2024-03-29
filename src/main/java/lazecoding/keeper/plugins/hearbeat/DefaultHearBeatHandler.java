package lazecoding.keeper.plugins.hearbeat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳处理器
 *
 * @author lazecoding
 */
@ChannelHandler.Sharable
public class DefaultHearBeatHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(DefaultHearBeatHandler.class);


    /**
     * 客户端在一定的时间没有动作就会触发这个事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object eventObj) {
        if (eventObj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) eventObj;
            if (event.state() == IdleState.READER_IDLE) {
                // 服务端 读空闲
                // 说明客户端没有发送数据来服务端，比如心跳
                logger.debug("DefaultHearBeatHandler.userEventTriggered IdleState.READER_IDLE channelId:[{}]", ctx.channel().id().asLongText());
                ctx.channel().close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                // 服务端 写空闲
                // doNothing...
            } else if (event.state() == IdleState.ALL_IDLE) {
                // 服务端 读写都空闲
                // doNothing...
            }
        }
    }
}
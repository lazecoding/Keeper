package personal.keeper.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Channe 工具类
 *
 * @author lazecoding
 */
public class ChannelUtil {

    /**
     * 获取 Channel
     */
    public static Channel getChannel(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return null;
        }
        return ctx.channel();
    }

    /**
     * 获取 ChannelId
     */
    public static String getChannelId(ChannelHandlerContext ctx) {
        return getChannelId(getChannel(ctx));
    }

    /**
     * 获取 ChannelId
     */
    public static String getChannelId(Channel channel) {
        if (null == channel) {
            return null;
        }
        return channel.id().asLongText();
    }



}

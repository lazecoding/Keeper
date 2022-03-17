package lazecoding.keeper.component;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 链接容器
 *
 * @author lazecoding
 */
public class GroupContainer {

    private GroupContainer() {
    }

    /**
     * ChannelId 和 ChannelHandlerContext 的映射。
     */
    public static Map<String, ChannelHandlerContext> CHANNEL_CONTEXT = new ConcurrentHashMap<>();

    /**
     * ChannelId 和 userTag 的映射。
     */
    public static Map<String, String> CHANNEL_USER = new ConcurrentHashMap<>();

    /**
     * userTag 和 一组ChannelId 的映射，一个用户可能多端登录。
     */
    public static Map<String, CopyOnWriteArraySet<String>> USER_CHANNEL = new ConcurrentHashMap<>();

}

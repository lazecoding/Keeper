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
    public final static Map<String, ChannelHandlerContext> CHANNEL_CONTEXT = new ConcurrentHashMap<>();

    /**
     * ChannelId 和 userId 的映射。
     */
    public final static Map<String, String> CHANNEL_USER = new ConcurrentHashMap<>();

    /**
     * userId 和 一组 ChannelId 的映射，一个用户可能多端登录。
     */
    public final static Map<String, CopyOnWriteArraySet<String>> USER_CHANNEL = new ConcurrentHashMap<>();

}

package personal.keeper.plugins.user;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.util.StringUtils;
import personal.keeper.component.GroupContainer;
import personal.keeper.util.ChannelUtil;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * User Manager
 *
 * @author lazecoding
 */
public class UserManager {

    /**
     * Init User
     */
    public static void initUser(String accessToken, ChannelHandlerContext ctx) {
        String userTag = new UserModel(accessToken).getUserTag();
        if(StringUtils.isEmpty(userTag)){
            return;
        }
        String channelId = ChannelUtil.getChannelId(ctx);
        // 维护某个链接属于哪个用户
        GroupContainer.CHANNEL_USER.put(channelId, userTag);
        // 维护某个用户的多端链接
        // 初始化，如果 key userTag 不存在会初始化一个空的 set，返回 null；如果存在就返回存储的 set 的引用，通过操作其引用达到线程安全。
        CopyOnWriteArraySet<String> anchorSet = GroupContainer.USER_CHANNEL.putIfAbsent(userTag, new CopyOnWriteArraySet<>());
        if (anchorSet == null) {
            anchorSet = GroupContainer.USER_CHANNEL.get(userTag);
        }
        anchorSet.add(channelId);
    }

    /**
     * Remove User
     */
    public static void removeUser(ChannelHandlerContext ctx) {
        String channelId = ChannelUtil.getChannelId(ctx);
        // 清除 userTag - channelId 双向映射
        String userTag = GroupContainer.CHANNEL_USER.get(channelId);
        CopyOnWriteArraySet<String> anchorSet = GroupContainer.USER_CHANNEL.get(userTag);
        if (anchorSet != null) {
            anchorSet.remove(channelId);
        }
        GroupContainer.CHANNEL_USER.remove(channelId);
    }

}

package personal.keeper.plugins.group;

import personal.keeper.config.Config;
import personal.keeper.util.BeanUtil;

import java.util.List;

/**
 * Group Manager
 *
 * @author lazecoding
 */
public class GroupManager {

    private static final GroupOperator GROUP_OPERATOR;

    static {
        // 切记，GroupManager 类不要托管给 Spring 容器
        if (Config.enableCluster) {
            GROUP_OPERATOR = BeanUtil.getBean("clusterGroupOperator", GroupOperator.class);
        } else {
            GROUP_OPERATOR = BeanUtil.getBean("localGroupOperator", GroupOperator.class);
        }
    }

    /**
     * 获取 GroupModel
     *
     * @param groupId GroupId
     * @return 返回 GroupModel
     */
    public static GroupModel findGroup(String groupId) {
        return GROUP_OPERATOR.findGroup(groupId);
    }

    /**
     * 获取 Group 内 UserId
     *
     * @param groupId GroupId
     * @return 返回 UserId List
     */
    public static List<String> findUserIdInGroup(String groupId) {
        return GROUP_OPERATOR.findUserIdInGroup(groupId);
    }

}

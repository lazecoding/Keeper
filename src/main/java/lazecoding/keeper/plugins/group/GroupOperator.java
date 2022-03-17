package lazecoding.keeper.plugins.group;

import java.util.List;

/**
 * Group Operator Interface
 *
 * @author lazecoding
 */
public interface GroupOperator {

    /**
     * 获取 GroupModel
     *
     * @param groupId GroupId
     * @return 返回 GroupModel
     */
    GroupModel findGroup(String groupId);


    /**
     * 获取 Group 内 UserId
     *
     * @param groupId GroupId
     * @return 返回 UserId List
     */
    List<String> findUserIdInGroup(String groupId);
}

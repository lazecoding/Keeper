package personal.keeper.plugins.group;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地 Group Operator 实现 TODO 待实现
 *
 * @author lazecoding
 */
@Component("localGroupOperator")
public class LocalGroupOperator implements GroupOperator {

    @Override
    public GroupModel findGroup(String groupId) {
        return new GroupModel();
    }

    @Override
    public List<String> findUserIdInGroup(String groupId) {
        List<String> userIds = new ArrayList<>();
        return userIds;
    }

}

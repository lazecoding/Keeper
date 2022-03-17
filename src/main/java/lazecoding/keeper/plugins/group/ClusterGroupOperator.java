package lazecoding.keeper.plugins.group;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 集群 Group Operator 实现。 TODO 业务方自行实现
 *
 * @author lazecoding
 */
@Component("clusterGroupOperator")
public class ClusterGroupOperator implements GroupOperator {

    @Override
    public GroupModel findGroup(String groupId) {
        return new GroupModel();
    }

    @Override
    public List<String> findUserIdInGroup(String groupId) {
        List<String> userIds = new ArrayList<>();
        userIds.add("test-group-userId");
        return userIds;
    }
}

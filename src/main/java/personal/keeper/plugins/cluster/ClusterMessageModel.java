package personal.keeper.plugins.cluster;

import personal.keeper.constant.DigitalConstant;

import java.util.Collections;
import java.util.List;

/**
 * Cluster Message Model
 *
 * @author lazecoding
 */
public class ClusterMessageModel {

    /**
     * 消息类型：1：发送给 User;2：广播
     */
    private int type;

    /**
     * 用户 Id 列表
     */
    private List<String> userIds;

    /**
     * 消息内容
     */
    private String message;

    public ClusterMessageModel() {
    }

    public static ClusterMessageModel getUserInstance(String userId, String message) {
        return getUserInstance(Collections.singletonList(userId), message);
    }

    public static ClusterMessageModel getUserInstance(List<String> userIds, String message) {
        ClusterMessageModel model = new ClusterMessageModel();
        model.type = DigitalConstant.ONE;
        model.userIds = userIds;
        model.message = message;
        return model;
    }

    public static ClusterMessageModel getBroadcastInstance(String message) {
        ClusterMessageModel model = new ClusterMessageModel();
        model.type = DigitalConstant.TWO;
        model.message = message;
        return model;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

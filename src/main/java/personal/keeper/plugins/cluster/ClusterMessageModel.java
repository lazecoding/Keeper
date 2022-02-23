package personal.keeper.plugins.cluster;

import personal.keeper.constant.DigitalConstant;

/**
 * Cluster Message Model
 *
 * @author lazecoding
 */
public class ClusterMessageModel {

    /**
     * 消息类型：1：发送给 User;2：发送给 Group；3：广播
     */
    private int type;

    /**
     * 用户 Id
     */
    private String userId;

    /**
     * 群组 Id
     */
    private String groupId;

    /**
     * 消息内容
     */
    private String message;

    public ClusterMessageModel() {
    }

    public static ClusterMessageModel getBroadcastInstance(int type, String message) {
        ClusterMessageModel model = new ClusterMessageModel();
        model.type = type;
        model.message = message;
        return model;
    }

    public static ClusterMessageModel getUserInstance(String userId, String message) {
        ClusterMessageModel model = new ClusterMessageModel();
        model.type = DigitalConstant.ONE;
        model.userId = userId;
        model.message = message;
        return model;
    }


    public static ClusterMessageModel getGroupInstance(String groupId, String message) {
        ClusterMessageModel model = new ClusterMessageModel();
        model.type = DigitalConstant.TWO;
        model.groupId = groupId;
        model.message = message;
        return model;
    }

    public static ClusterMessageModel getBroadcastInstance(String message) {
        ClusterMessageModel model = new ClusterMessageModel();
        model.type = DigitalConstant.THREE;
        model.message = message;
        return model;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package lazecoding.keeper.model;

import java.io.Serializable;
import java.util.List;

/**
 * WebSocketMqMessageBean
 *
 * @author lazecoding
 */
public class WebSocketMqMessageBean implements Serializable {

    private static final long serialVersionUID = 9188935005730546428L;

    /**
     * 消息体
     */
    private String message;

    /**
     * 接收的用户
     */
    private List<String> userIds;

    /**
     * 是否广播
     */
    private Boolean isBroadcast;

    public WebSocketMqMessageBean() {
    }

    public WebSocketMqMessageBean(String message, List<String> userIds, Boolean isBroadcast) {
        this.message = message;
        this.userIds = userIds;
        this.isBroadcast = isBroadcast;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Boolean getBroadcast() {
        return isBroadcast;
    }

    public void setBroadcast(Boolean broadcast) {
        isBroadcast = broadcast;
    }

    @Override
    public String toString() {
        return "WebSocketMqMessageBean{" +
                "message='" + message + '\'' +
                ", userIds=" + userIds +
                ", isBroadcast=" + isBroadcast +
                '}';
    }
}


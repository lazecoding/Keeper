package lazecoding.keeper.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 把 WebSocketRequest 填充更多属性，用于发送给其他服务端
 *
 * @author lazecoding
 */
public class ClientMessageBean implements Serializable {

    private static final long serialVersionUID = 9188935005730546428L;


    /**
     * 应用名 （必填）
     */
    private String app = "";

    /**
     * 事件类型 （必填）
     */
    private String event = "";

    /**
     * 消息体
     */
    private Object data;

    /**
     * 用户 Id
     */
    private String userId = "";

    /**
     * 时间戳
     */
    private long timestamp = 0L;

    public ClientMessageBean() {
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public ClientMessageBean(String app, String event, Object data, String userId) {
        this.app = app;
        this.event = event;
        this.data = data;
        this.userId = userId;
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ClientMessageBean{" +
                "app='" + app + '\'' +
                ", event='" + event + '\'' +
                ", data=" + data +
                ", userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

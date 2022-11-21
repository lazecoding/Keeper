package lazecoding.keeper.model;

import java.io.Serializable;

/**
 * WebSocketRequest
 *
 * {
 *     "app": "",
 *     "event": "",
 *     "data": ""
 * }
 *
 * @author lazecoding
 */
public class WebSocketRequest implements Serializable {


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

    @Override
    public String toString() {
        return "WebSocketRequest{" +
                "app='" + app + '\'' +
                ", event='" + event + '\'' +
                ", data=" + data +
                '}';
    }
}

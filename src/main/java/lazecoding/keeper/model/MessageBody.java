package lazecoding.keeper.model;

import lazecoding.keeper.exception.NilParamException;
import lazecoding.keeper.util.UUIDUtil;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * MessageBody
 *
 * @author lazecoding
 */
public class MessageBody implements Serializable {

    private static final long serialVersionUID = 9188935005730546428L;


    /**
     * 应用名(必填)
     */
    private String app = "default";

    /**
     * 事件类型 (必填)
     */
    private String event = "";

    /**
     * 消息体
     */
    private Object data;

    /**
     * traceId
     */
    private String traceId = "";

    public MessageBody() {
        this.app = "default";
        this.traceId = UUIDUtil.getUUID();
    }

    public MessageBody(String app, String event, Object data) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        if (!StringUtils.hasText(event)) {
            throw new NilParamException("event is nil.");
        }
        this.app = app;
        this.event = event;
        this.data = data;
        this.traceId = UUIDUtil.getUUID();
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        this.app = app;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        if (!StringUtils.hasText(event)) {
            throw new NilParamException("event is nil.");
        }
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "app='" + app + '\'' +
                ", event='" + event + '\'' +
                ", data=" + data +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}

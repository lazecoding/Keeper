package lazecoding.keeper.model;

import lazecoding.keeper.exception.NilParamException;
import lazecoding.keeper.util.UUIDUtil;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 响应模型
 *
 * @author lazecoding
 */
public class WebSocketResult implements Serializable {

    private static final long serialVersionUID = 9188935005730546428L;

    /**
     * 应用名 （必填）
     */
    private String app = "default";

    /**
     * 事件类型 （必填）
     */
    private String event = "";

    /**
     * 消息体
     */
    private Object data;

    /**
     * 时间戳
     */
    private long timestamp = 0L;

    /**
     * traceId
     */
    private String traceId = "";

    public WebSocketResult() {
        this.app = "default";
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        this.traceId = UUIDUtil.getUUID();
    }

    public WebSocketResult(String app, String event, Object data) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        if (!StringUtils.hasText(event)) {
            throw new NilParamException("event is nil.");
        }
        this.app = app;
        this.event = event;
        this.data = data;
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        this.traceId = UUIDUtil.getUUID();
    }

    public WebSocketResult(String app, String event, Object data, String traceId) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        if (!StringUtils.hasText(event)) {
            throw new NilParamException("event is nil.");
        }
        if (!StringUtils.hasText(traceId)) {
            throw new NilParamException("source app traceId is nil.");
        }
        this.app = app;
        this.event = event;
        this.data = data;
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        this.traceId = traceId;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}

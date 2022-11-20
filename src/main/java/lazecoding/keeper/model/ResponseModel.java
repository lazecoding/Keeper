package lazecoding.keeper.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 响应模型，如：{"type":"M","content":"1","timestamp":1637645936927}
 *
 * @author lazecoding
 */
public class ResponseModel implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型:
     * E 请求异常，C 循环任务，B 业务请求
     */
    private String type = "";

    /**
     * 响应内容 json 格式
     */
    private Object content;

    /**
     * 时间戳
     */
    private long timestamp = 0L;

    public ResponseModel(String type, Object content) {
        this.type = type;
        this.content = content;
        this.timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

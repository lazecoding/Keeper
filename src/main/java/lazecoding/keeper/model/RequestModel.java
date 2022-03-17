package lazecoding.keeper.model;

/**
 * 请求模型
 * <br/>
 * 结构：{"type":"M","content":""}
 *
 * @author lazecoding
 */
public class RequestModel {

    /**
     * 请求类型：M 获取连接数;B 业务请求
     */
    private String type = "";

    /**
     * 请求内容 json 格式
     */
    private String content = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

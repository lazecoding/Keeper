package lazecoding.keeper.constant;

/**
 * SSE 自定义事件
 *
 * @author lazecoding
 */
public enum SseCustomEvents {

    HEALTH("health", "链接健康检查事件"),

    SSE_ID("SSE-ID", "推送链接 sseId");


    /**
     * 事件名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    SseCustomEvents(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}

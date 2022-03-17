package lazecoding.keeper.constant;

/**
 * 响应类型
 * <p>
 * E 请求异常，C 周期任务，B 业务请求
 * <p>
 * E、P 固定，C、B 根据业务类型加上数字标识。
 *
 * @author lazecoding
 */
public enum ResponseType {
    /**
     * 请求异常
     */
    EXCEPTION("E", "请求异常"),

    /**
     * MONITOR，获取连接数
     */
    MONITOR("M", "MONITOR"),

    /**
     * 周期推送
     */
    CYCLE("C", "周期推送");

    private String code;

    private String describe;

    ResponseType(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}

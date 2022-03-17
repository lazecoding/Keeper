package lazecoding.keeper.constant;

/**
 * 请求类型
 * <p>
 * M 获取连接数;B 业务请求
 *
 * @author lazecoding
 */
public enum RequestType {
    /**
     * MONITOR，获取连接数
     */
    MONITOR("M", "MONITOR"),

    B_3("B3", "业务-1");

    private String code;

    private String describe;

    private RequestType(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

    /**
     * 是否包含某个枚举
     *
     * @param code
     * @return
     */
    public static boolean contain(String code) {
        for (RequestType item : RequestType.values()) {
            if (code.equals(item.code)) {
                return true;
            }
        }
        return false;
    }
}

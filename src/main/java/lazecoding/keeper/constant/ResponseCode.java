package lazecoding.keeper.constant;

/**
 *
 * 响应 code
 *
 * @author lazecoding
 */
public enum ResponseCode {
    /**
     * 请求异常
     */
    EXCEPTION("EXCEPTION", "请求异常"),

    /**
     * 请求成功
     */
    SUCCESS("SUCCESS", "请求成功"),

    /**
     * 本地在线链接数量
     */
    LOCAL_ONLINE_NUM("O1", "本地在线链接数量");

    private String code;

    private String describe;

    ResponseCode(String code, String describe) {
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
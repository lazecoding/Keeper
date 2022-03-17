package lazecoding.keeper.constant;

/**
 * 查询参数列表
 *
 * @author lazecoding
 */
public enum QueryKeys {

    /**
     * 用户参数列表的 Key，access-token
     */
    AT("access-token");


    private String describe;

    private QueryKeys(String describe) {
        this.describe = describe;
    }
}

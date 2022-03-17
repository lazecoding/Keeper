package lazecoding.keeper.plugins.user;

/**
 * 用户
 *
 * @author lazecoding
 */
public class UserModel {

    /**
     * 用户标识，用于标识唯一的用户，如 userId。
     */
    private String userTag = "";

    /**
     * 访问标识，用于解析用户信息。即：access-token
     */
    private String accessToken = "";

    public UserModel(String accessToken) {
        this.accessToken = accessToken;
        setUserTag();
    }

    public String getUserTag() {
        return userTag;
    }

    /**
     *  TODO 根据 accessToken 获取 userTag
     */
    private void setUserTag() {
        this.userTag = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

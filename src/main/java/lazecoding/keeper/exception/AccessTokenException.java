package lazecoding.keeper.exception;

/**
 * 用户 token 异常
 *
 * @author lazecoding
 */
public class AccessTokenException extends RuntimeException {
    public AccessTokenException(String msg) {
        super(msg);
    }
}

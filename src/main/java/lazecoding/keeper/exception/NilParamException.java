package lazecoding.keeper.exception;

/**
 * 空属性异常
 *
 * @author lazecoding
 */
public class NilParamException extends RuntimeException {
    public NilParamException(String msg) {
        super(msg);
    }
}

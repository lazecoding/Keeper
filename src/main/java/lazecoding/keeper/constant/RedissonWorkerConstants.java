package lazecoding.keeper.constant;

/**
 * RedissonWorkerConstants
 *
 * @author lazecoding
 */
public enum RedissonWorkerConstants {

    /**
     * DEFAULT_EXECUTOR_SERVICE
     */
    DEFAULT_EXECUTOR_SERVICE("DEFAULT_EXECUTOR_SERVICE", 3);

    private String name;

    private int size;

    RedissonWorkerConstants(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}

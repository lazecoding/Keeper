package lazecoding.keeper.constant;

import java.util.concurrent.TimeUnit;

/**
 * CacheConstants
 *
 * @author lazecoding
 */
public enum CacheConstants {

    /**
     * 下线服务清楚任务
     */
    SERVER_CLEAN_TASK("Keeper:SERVER_CLEAN_TASK:", -1L, TimeUnit.SECONDS),

    /**
     * 服务保活
     */
    SERVER_KEEP("Keeper:SERVER_KEEP:", 60 * 10L, TimeUnit.SECONDS),

    /**
     * 服务保活集合
     */
    SERVER_KEEP_SET("Keeper:SERVER_KEEP_SET", -1L, TimeUnit.SECONDS);

    private String name;

    private Long ttl;

    private TimeUnit timeUnit;

    private CacheConstants(String name, Long ttl, TimeUnit timeUnit) {
        this.name = name;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
    }

    public String getName() {
        return name;
    }

    public Long getTtl() {
        return ttl;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}

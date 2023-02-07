package lazecoding.keeper.util;

import org.redisson.api.RedissonClient;
import org.springframework.util.ObjectUtils;

/**
 * RedissonClientUtil
 *
 * @author lazecoding
 */
public class RedissonClientUtil {

    private static RedissonClient redissonClient;

    private RedissonClientUtil() {
    }

    public static RedissonClient getRedissonClient() {
        if (ObjectUtils.isEmpty(redissonClient)) {
            synchronized (RedissonClientUtil.class) {
                redissonClient = BeanUtil.getBean(RedissonClient.class);
            }
        }
        return redissonClient;
    }

}

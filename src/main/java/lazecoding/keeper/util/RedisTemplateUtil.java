package lazecoding.keeper.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

/**
 * RedisTemplateUtil
 *
 * @author lazecoding
 */
public class RedisTemplateUtil {

    private static RedisTemplate redisTemplate;

    private RedisTemplateUtil() {
    }

    public static RedisTemplate getRedisTemplate() {
        if (ObjectUtils.isEmpty(redisTemplate)) {
            synchronized (RedisTemplateUtil.class) {
                redisTemplate = BeanUtil.getBean("redisTemplate", RedisTemplate.class);
            }
        }
        return redisTemplate;
    }
}

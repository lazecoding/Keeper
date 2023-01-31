package lazecoding.keeper.service;

import lazecoding.keeper.constant.CacheConstants;
import lazecoding.keeper.util.RedisTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerKeepService
 *
 * @author lazecoding
 */
@Service
public class ServerKeepService {

    private final static Logger logger = LoggerFactory.getLogger(ServerKeepService.class);

    public void clean() {
        RedisTemplate redisTemplate = RedisTemplateUtil.getRedisTemplate();
        Cursor<String> cursors = redisTemplate.opsForSet().scan(CacheConstants.SERVER_KEEP_SET.getName(), ScanOptions.NONE);
        List<String> needCleanIds = new ArrayList<>();
        while (cursors.hasNext()) {
            String uid = cursors.next();
            boolean hasKey = redisTemplate.hasKey(CacheConstants.SERVER_KEEP.getName() + uid);
            if (!hasKey) {
                needCleanIds.add(uid);
            }
        }
        // 存在下线服务
        if (CollectionUtils.isEmpty(needCleanIds)) {
            logger.debug("ServerKeepService : no server needs to be cleaned up.");
        } else {
            try {
                redisTemplate.opsForSet().remove(CacheConstants.SERVER_KEEP_SET.getName(), needCleanIds.toArray());
            } catch (Exception e) {
                logger.error("ServerKeepService : clean server exception.", e);
                throw new RuntimeException(e);
            }
            logger.debug("ServerKeepService : needs to clean server:[{}].", needCleanIds.toString());
        }
    }

}

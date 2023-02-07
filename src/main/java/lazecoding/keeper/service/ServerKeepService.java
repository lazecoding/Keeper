package lazecoding.keeper.service;

import lazecoding.keeper.constant.CacheConstants;
import lazecoding.keeper.util.RedissonClientUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
        RedissonClient redissonClient = RedissonClientUtil.getRedissonClient();
        RSet<String> serverSets = redissonClient.getSet(CacheConstants.SERVER_KEEP_SET.getName());
        List<String> needCleanIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(serverSets) && !serverSets.isEmpty()) {
            for (String uid : serverSets) {
                RBucket<String> bucket = redissonClient.getBucket(CacheConstants.SERVER_KEEP.getName() + uid);
                boolean hasKey = bucket.isExists();
                if (!hasKey) {
                    needCleanIds.add(uid);
                }
            }
        }
        // 存在下线服务
        if (CollectionUtils.isEmpty(needCleanIds)) {
            logger.debug("ServerKeepService : no server needs to be cleaned up.");
        } else {
            try {
                needCleanIds.forEach(serverSets::remove);
            } catch (Exception e) {
                logger.error("ServerKeepService : clean server exception.", e);
                throw new RuntimeException(e);
            }
            logger.debug("ServerKeepService : needs to clean server:[{}].", needCleanIds.toString());
        }
    }

}

package lazecoding.keeper.plugins.mq;

import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import lazecoding.keeper.constant.DigitalConstant;
import lazecoding.keeper.util.BeanUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Pulsar 广播订阅组件
 * <p>
 * 启用广播主题：
 * 1. 初始化并启动广播订阅。
 * String localSubscriptionName = BroadcastComponent.initBroadcastTopic(topicName, subscriptionName);
 * 2. 使用 localSubscriptionName 作为订阅创建消费者。
 *
 * @author lazecoding
 */
@Component("keeper.BroadcastComponent")
public class BroadcastComponent {

    private final static Logger logger = LoggerFactory.getLogger(BroadcastComponent.class);

    /**
     * 初始化并启动广播订阅
     *
     * @param topicName        Topic Name
     * @param subscriptionName Subscription Name
     * @return New Subscription Name
     * @throws PulsarClientException
     */
    public static String initBroadcastTopic(String topicName, String subscriptionName) throws PulsarClientException {
        if (StringUtils.isEmpty(topicName) || StringUtils.isEmpty(subscriptionName)) {
            throw new PulsarClientException("topicName | subscriptionName is Nil.");
        }
        String localSubscriptionName = subscriptionName + UUID.randomUUID().toString().replaceAll("-", "");

        // 启动广播订阅检测：key 规则：broadcast:{subscriptionName}
        // 1.每台机器维护本机订阅入 Redis，并初始化生命周期。
        // 2.周期为 Redis 更新生命周期。
        // 3.启动线程定期扫描改主题的订阅，如果不在 Redis 中则清除。
        RedisTemplate redisTemplate = BeanUtil.getBean("redisTemplate", RedisTemplate.class);
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("executor-broadcast");
            thread.setDaemon(true);
            return thread;
        });
        // 1.init local subscription
        redisTemplate.opsForValue().setIfAbsent("broadcast:" + localSubscriptionName, "broadcast", DigitalConstant.TWENTY, TimeUnit.SECONDS);
        logger.info("init local subscription ready.");
        // 2.reset local subscription task - 20s ttl, 15s 周期。
        scheduledExecutor.scheduleWithFixedDelay(() -> {
            boolean expire = redisTemplate.expire("broadcast:" + localSubscriptionName, DigitalConstant.TWENTY, TimeUnit.SECONDS);
            logger.debug("expire topic:{} subscription:{} ttl:{} isSuccess:{}", topicName, localSubscriptionName, DigitalConstant.TWENTY, expire);
        }, DigitalConstant.FIFTEEN, DigitalConstant.FIFTEEN, TimeUnit.SECONDS);
        logger.info("reset local subscription task ready.");

        // 3. compare and clean
        scheduledExecutor.scheduleWithFixedDelay(() -> {
            // init local subscription - 5 min
            try {
                List<String> subscriptions = PulsarManager.pulsarAdmin.topics().getSubscriptions(topicName);
                if (CollectionUtils.isEmpty(subscriptions)) {
                    return;
                }
                for (String subscription : subscriptions) {
                    boolean hasKey = redisTemplate.hasKey("broadcast:" + subscription);
                    if (!hasKey) {
                        // remove this subscription
                        PulsarManager.pulsarAdmin.topics().deleteSubscription(topicName, subscription);
                        logger.debug("remove topic:{} subscription:{}", topicName, subscription);
                    }
                }
            } catch (PulsarAdminException e) {
                logger.error("clean nil subscription in topic error.", e.getCause().toString());
            }
        }, DigitalConstant.FIVE, DigitalConstant.FIVE, TimeUnit.MINUTES);
        logger.info("compare and clean nil subscription task ready.");
        return localSubscriptionName;
    }

}

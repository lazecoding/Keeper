package personal.keeper.plugins.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import personal.keeper.component.AsynTaskExecutor;
import personal.keeper.constant.DigitalConstant;
import personal.keeper.plugins.cluster.ClusterManager;
import personal.keeper.plugins.cluster.ClusterMessageModel;
import personal.keeper.util.BeanUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Pulsar Consumer
 *
 * @author lazecoding
 */
@Component("keeper.PulsarConsumer")
public class PulsarConsumer {

    private final static Logger logger = LoggerFactory.getLogger(PulsarConsumer.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Init Message Sync Consumer
     */
    public static void initMessageSyncConsumer() throws PulsarClientException {
        String subscriptionName = MqConstant.MESSAGE_SYNC.getSubscriptionName() + UUID.randomUUID().toString().replaceAll("-", "");
        String topicName = MqConstant.MESSAGE_SYNC.getTopicName();
        PulsarInit.client.newConsumer()
                .topic(topicName)
                .subscriptionName(subscriptionName)
                .ackTimeout(10, TimeUnit.SECONDS)
                .subscriptionType(SubscriptionType.Shared)
                .messageListener((consumer, message) -> {
                    // 消息转发到业务线程池处理，增加 MQ 吞吐量
                    String messageData = new String(message.getData());
                    logger.debug("cost message:" + messageData);
                    AsynTaskExecutor.submitTask(() -> {
                        try {
                            ClusterMessageModel clusterMessageModel = MAPPER.readValue(messageData, ClusterMessageModel.class);
                            ClusterManager.consumeClusterMessage(clusterMessageModel);
                            consumer.acknowledge(message);
                        } catch (JsonProcessingException | PulsarClientException e) {
                            e.printStackTrace();
                        }
                    });
                })
                .subscribe();
        logger.info("init Message Sync Consumer ready.");

        // 启动广播订阅检测：key 规则：message-sync:{subscriptionName}
        // 1.每台机器维护本机订阅入 Redis，并初始化生命周期。
        // 2.周期为 Redis 更新生命周期。
        // 3.启动线程定期扫描改主题的订阅，如果不在 Redis 中则清除。

        RedisTemplate redisTemplate = BeanUtil.getBean("redisTemplate", RedisTemplate.class);
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("executor-message-sync");
            thread.setDaemon(true);
            return thread;
        });
        String finalSubscriptionName = subscriptionName;
        // 1.init local subscription
        redisTemplate.opsForValue().setIfAbsent("message-sync:" + finalSubscriptionName, "Message Sync", DigitalConstant.TWENTY, TimeUnit.SECONDS);
        logger.info("init local subscription ready.");
        // 2.reset local subscription task - 20s ttl, 15s 周期。
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                boolean expire = redisTemplate.expire("message-sync:" + finalSubscriptionName, DigitalConstant.TWENTY, TimeUnit.SECONDS);
                logger.info("expire topic:{} subscription:{} ttl:{} isSuccess:{}", topicName, subscriptionName, DigitalConstant.TWENTY, expire);
            }
        }, DigitalConstant.FIFTEEN, DigitalConstant.FIFTEEN, TimeUnit.SECONDS);
        logger.info("reset local subscription task ready.");

        // 3. compare and clean
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // init local subscription - 5 min
                try {
                    List<String> subscriptions = PulsarManager.pulsarAdmin.topics().getSubscriptions(topicName);
                    if (CollectionUtils.isEmpty(subscriptions)) {
                        return;
                    }
                    for (String subscription : subscriptions) {
                        boolean hasKey = redisTemplate.hasKey("message-sync:" + subscription);
                        if (!hasKey) {
                            // remove this subscription
                            PulsarManager.pulsarAdmin.topics().deleteSubscription(topicName, subscription);
                            logger.info("remove topic:{} subscription:{}", topicName, subscription);
                        }
                    }
                } catch (PulsarAdminException e) {
                    logger.error("clean nil subscription in topic error.", e);
                }
            }
        }, DigitalConstant.FIVE, DigitalConstant.FIVE, TimeUnit.MINUTES);
        logger.info("compare and clean nil subscription task ready.");
    }

}

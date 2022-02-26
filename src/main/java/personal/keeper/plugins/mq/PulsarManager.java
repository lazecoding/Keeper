package personal.keeper.plugins.mq;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Pulsar Manager
 *
 * @author lazecoding
 */
@Component("keeper.PulsarManager")
public class PulsarManager {

    private final static Logger logger = LoggerFactory.getLogger(PulsarManager.class);

    public static PulsarAdmin pulsarAdmin;

    /**
     * Init PulsarAdmin
     *
     * @throws PulsarClientException
     */
    public static void init() throws PulsarClientException {
        pulsarAdmin = PulsarAdmin.builder()
                .serviceHttpUrl(PulsarConfig.httpUrl)
                .authentication(StringUtils.isEmpty(PulsarConfig.token) ? null : AuthenticationFactory.token(PulsarConfig.token))
                .build();
    }

    /**
     * TODO 清除没有消费者的可变订阅
     * <p>
     * 每个服务维护本地订阅并注册到 Redis，设置生命周期并续约。
     * 另一方面，一个专门的线程用于对比主题下订阅和 Redis 中注册的订阅差集，并清理。
     *
     * @throws PulsarAdminException
     */
    public static void cleanNilSubscriptions() throws PulsarAdminException {

        // pulsarAdmin.topics().getList("")

        /*String topic = "persistent://my-tenant/my-namespace/my-topic";
        String subscriptionName = "my-subscription";
        pulsarAdmin.topics().deleteSubscription(topic, subscriptionName);*/

        List<String> subscriptions = pulsarAdmin.topics().getSubscriptions(MqConstant.MESSAGE_SYNC.getTopicName());
        logger.info("PulsarManager subscriptions:" + subscriptions.toString());

        List<String> topics = pulsarAdmin.topics().getList(PulsarConfig.tenant + "/" + PulsarConfig.namespace);
        logger.info("PulsarManager topics:" + topics.toString());

        List<String> namespaces = pulsarAdmin.namespaces().getNamespaces(PulsarConfig.tenant);
        logger.info("PulsarManager namespaces:" + namespaces.toString());

    }
}

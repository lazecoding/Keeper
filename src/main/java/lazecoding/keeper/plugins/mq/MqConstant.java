package lazecoding.keeper.plugins.mq;

/**
 * Mq 常量
 *
 * @author lazecoding
 */
public enum MqConstant {

    /**
     * MESSAGE_SYNC 消息同步
     */
    MESSAGE_SYNC("topic-message-sync", "subscription-message-sync-");

    private String topicName;

    private String subscriptionName;

    public String getTopicName() {
        return PulsarConfig.tenant + "/" + PulsarConfig.namespace + "/" + topicName;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    MqConstant(String topicName, String subscriptionName) {
        this.topicName = topicName;
        this.subscriptionName = subscriptionName;
    }
}

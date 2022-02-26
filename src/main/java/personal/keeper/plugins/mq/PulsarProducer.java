package personal.keeper.plugins.mq;

import org.apache.pulsar.client.api.CompressionType;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Pulsar Producer
 *
 * @author lazecoding
 */
@Component("keeper.PulsarProducer")
public class PulsarProducer {

    private final static Logger logger = LoggerFactory.getLogger(PulsarProducer.class);

    private static Map<String, Producer<byte[]>> producers = new ConcurrentHashMap<>();

    /**
     * init PulsarProducer
     */
    private static void init(String topicName) throws PulsarClientException {
        Producer<byte[]> producer = PulsarInit.client.newProducer()
                .topic(topicName)
                .enableBatching(true)
                .compressionType(CompressionType.LZ4)
                .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
                .sendTimeout(0, TimeUnit.SECONDS)
                .blockIfQueueFull(true)
                .create();
        producers.put(topicName, producer);
    }

    /**
     * Get PulsarProducer Instance
     */
    public static Producer<byte[]> getInstance(String topicName) {
        Producer<byte[]> producer;
        if (Objects.isNull(producer = producers.get(topicName))) {
            try {
                synchronized (PulsarProducer.class) {
                    init(topicName);
                }
                producer = producers.get(topicName);
            } catch (PulsarClientException e) {
                logger.error("init PulsarProducer error", e);
                return null;
            }
        }
        return producer;
    }

}
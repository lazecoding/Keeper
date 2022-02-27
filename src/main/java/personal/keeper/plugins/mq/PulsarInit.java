package personal.keeper.plugins.mq;

import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import personal.keeper.component.AsynTaskExecutor;
import personal.keeper.util.BeanUtil;

import java.util.UUID;

/**
 * Pulsar Init
 *
 * @author lazecoding
 */
@Component("keeper.pulsarInit")
public class PulsarInit {

    private final static Logger logger = LoggerFactory.getLogger(PulsarInit.class);

    public static PulsarClient client;

    public static Environment environment;

    /**
     * init Pulsar
     */
    public static void init() throws PulsarClientException, PulsarAdminException {
        // 1.init config
        initConfig();

        // 2.init PulsarManager
        PulsarManager.init();

        // 3.init PulsarClient
        initClient();

        // 4.init PulsarConsumer
        initConsumer();
    }

    /**
     * init PulsarConfig
     *
     * @throws PulsarClientException
     */
    public static void initConfig() throws PulsarClientException {
        environment = BeanUtil.getBean(Environment.class);
        if (StringUtils.isEmpty(PulsarConfig.pulsarUrl = environment.getProperty("pulsar.server-url"))) {
            throw new PulsarClientException("Init Pulsar url Exception");
        }
        if (StringUtils.isEmpty(PulsarConfig.httpUrl = environment.getProperty("pulsar.http-url"))) {
            throw new PulsarClientException("Init Pulsar HTTP Url Exception");
        }
        if (StringUtils.isEmpty(PulsarConfig.tenant = environment.getProperty("pulsar.tenant"))) {
            PulsarConfig.tenant = "public";
        }
        if (StringUtils.isEmpty(PulsarConfig.namespace = environment.getProperty("pulsar.namespace"))) {
            PulsarConfig.namespace = "default";
        }
        PulsarConfig.token = environment.getProperty("pulsar.token");
        logger.info("init PulsarConfig ready. \n{}", PulsarConfig.getString());
    }

    /**
     * init PulsarClient
     *
     * @throws PulsarClientException
     */
    public static void initClient() throws PulsarClientException {
        client = PulsarClient.builder()
                .serviceUrl(PulsarConfig.pulsarUrl)
                .authentication(StringUtils.isEmpty(PulsarConfig.token) ? null : AuthenticationFactory.token(PulsarConfig.token))
                .build();
        logger.info("init PulsarClient ready.");
    }

    /**
     * init PulsarConsumer
     *
     * @throws PulsarClientException
     */
    public static void initConsumer() throws PulsarClientException {

        // Init Message Sync Consumer
        PulsarConsumer.initMessageSyncConsumer();

        logger.info("init all PulsarConsumer ready.");
    }
}

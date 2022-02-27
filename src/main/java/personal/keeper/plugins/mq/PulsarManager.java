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
        logger.info("init PulsarAdmin ready.");
    }
}

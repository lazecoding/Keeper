package lazecoding.keeper.util.amqp;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * AmqpConfig
 *
 * @author lazecoding
 */
@Configuration
public class AmqpConfig {

    @Value("${amqp.rabbitmq.host}")
    private String address;
    @Value("${amqp.rabbitmq.port}")
    private String port;
    @Value("${amqp.rabbitmq.username}")
    private String username;
    @Value("${amqp.rabbitmq.password}")
    private String password;
    @Value("${amqp.rabbitmq.virtual-host}")
    private String virtualhost;

    @Bean
    public ConnectionFactory connectionFactory() {
        if (!StringUtils.hasText(address) || !StringUtils.hasText(port) ||
                !StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        if (StringUtils.hasText(address)) {
            virtualhost = "/";
        }
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(address + ":" + port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualhost);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        if (ObjectUtils.isEmpty(connectionFactory)) {
            return null;
        }
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

}

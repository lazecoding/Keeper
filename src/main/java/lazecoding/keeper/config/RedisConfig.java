package lazecoding.keeper.config;

import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RedisConfig
 *
 * @author lazecoding
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConfigProperties redisConfigProperties;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient getRedissonClient() {
        boolean enableCluster = redisConfigProperties.getEnableCluster();
        Config config = new Config();
        // 编码类型
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.setTransportMode(TransportMode.NIO);
        String connectionTimeoutConfig = redisConfigProperties.getConnectionTimeout();
        if (!StringUtils.hasText(connectionTimeoutConfig)) {
            connectionTimeoutConfig = RedisConfigProperties.DefaultConfig.connectionTimeout;
        }
        int connectionTimeout = Integer.parseInt(connectionTimeoutConfig);
        if (enableCluster) {
            List<String> clusterNodes = redisConfigProperties.getCluster().getNodeAddresses();
            ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));
            //设置密码
            clusterServersConfig.setPassword(redisConfigProperties.getPassword());
            //redis连接心跳检测，防止一段时间过后，与redis的连接断开
            clusterServersConfig.setPingConnectionInterval(connectionTimeout);
        } else {
            SingleServerConfig singleServerConfig = config.useSingleServer()
                    .setAddress(redisConfigProperties.getSingle().getAddress())
                    .setDatabase(redisConfigProperties.getSingle().getDatabase());
            //设置密码
            singleServerConfig.setPassword(redisConfigProperties.getPassword());
            //redis连接心跳检测，防止一段时间过后，与redis的连接断开
            singleServerConfig.setPingConnectionInterval(connectionTimeout);
        }

        // init node worker : 使用 ExecutorService 准备
        RedissonNodeConfig nodeConfig = new RedissonNodeConfig(config);
        Map<String, Integer> workers = Collections.singletonMap("Default_Executor_Service", 3);
        nodeConfig.setExecutorServiceWorkers(workers);
        RedissonNode node = RedissonNode.create(nodeConfig);
        node.start();
        return Redisson.create(config);
    }
}

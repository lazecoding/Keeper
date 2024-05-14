package lazecoding.keeper.listener;

import lazecoding.keeper.bootstarp.Server;
import lazecoding.keeper.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动后执行
 *
 * @author lazecoding
 */
@Component
public class KeeperStartupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(KeeperStartupRunner.class);


    @Override
    public void run(ApplicationArguments args) {
        try {
            // 0.初始化配置
            Server.init();

            // 1.启动 WebSocket Server
            Server.start();

            // 2. MQ
            if (Config.enableCluster) {
                logger.info("集群模式运行中");
                MqListener.init();
            } else {
                logger.info("单机模式运行中");
            }
        } catch (Exception e) {
            logger.error("ApplicationRunner Exception", e);
        }
    }
}

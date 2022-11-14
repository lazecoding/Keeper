package lazecoding.keeper.listener;

import lazecoding.keeper.bootstarp.Server;
import lazecoding.keeper.plugins.batch.BatchExecutor;
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

            // 2.注册 BatchExecutor
            BatchExecutor.registered();
        } catch (Exception e) {
            logger.error("ApplicationRunner Exception", e);
        }
    }
}

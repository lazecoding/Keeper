package lazecoding.keeper.listener;

import lazecoding.keeper.bootstarp.Server;
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

    @Override
    public void run(ApplicationArguments args) {
        // 启动 WebSocket Server
        Server.start();
    }
}

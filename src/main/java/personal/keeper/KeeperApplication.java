package personal.keeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import personal.keeper.bootstarp.Server;

/**
 * Boot 启动类
 *
 * @author lazecoding
 */
@SpringBootApplication
public class KeeperApplication {

    public static void main(String[] args) {

        SpringApplication.run(KeeperApplication.class, args);

        Server.start();
    }

}

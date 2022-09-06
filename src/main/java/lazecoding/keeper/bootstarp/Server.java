package lazecoding.keeper.bootstarp;

import lazecoding.keeper.config.Config;
import lazecoding.keeper.config.PluginInfo;
import lazecoding.keeper.config.ServerInfo;
import lazecoding.keeper.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * Server 入口
 *
 * @author lazecoding
 */
public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void start() {
        // init
        init();

        // doStart
        Executors.newSingleThreadExecutor().submit(Bootstrap::doStart);
    }

    private static void init() {
        // init config
        initConfig();

    }

    /**
     * init config
     */
    private static void initConfig() {
        // 初始化 server-config
        ServerInfo serverInfo = BeanUtil.getBean("serverInfo", ServerInfo.class);
        if (serverInfo != null) {
            if (!StringUtils.isEmpty(serverInfo.getContextPath())) {
                Config.contextPath = serverInfo.getContextPath();
            }
            if (!StringUtils.isEmpty(serverInfo.getServerPort())) {
                Config.serverPort = serverInfo.getServerPort();
            }
            if (!StringUtils.isEmpty(serverInfo.getHttpObjectLength())) {
                Config.httpObjectLength = serverInfo.getHttpObjectLength();
            }
            if (!StringUtils.isEmpty(serverInfo.getSoBacklog())) {
                Config.soBacklog = serverInfo.getSoBacklog();
            }
        }

        // 初始化 plugin-config
        PluginInfo pluginInfo = BeanUtil.getBean("pluginInfo", PluginInfo.class);
        if (pluginInfo != null) {
            Config.enableHearBeat = pluginInfo.getEnableHearBeat();
            Config.hearBeatCycle = pluginInfo.getHearBeatCycle();
            Config.enableEventLoop = pluginInfo.getEnableEventLoop();
            Config.eventLoopCycle = pluginInfo.getEventLoopCycle();
        }

        // 初始化 uid
        Config.uid = UUID.randomUUID().toString().replaceAll("-", "");

        // 打印
        logger.info("init server config ready.\n{}", Config.getString());
    }

}

package personal.keeper.bootstarp;

import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import personal.keeper.config.Config;
import personal.keeper.config.PluginInfo;
import personal.keeper.config.ServerInfo;
import personal.keeper.plugins.mq.PulsarInit;
import personal.keeper.util.BeanUtil;

import java.util.UUID;

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
        Bootstrap.doStart();
    }

    public static void init() {
        // init config
        initCongig();

        // init message queue
        if (Config.enableCluster) {
           initMessageQueue();
        }

    }

    /**
     * init config
     */
    public static void initCongig() {
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
            Config.enableUser = pluginInfo.getEnableUser();
            Config.enableGroup = pluginInfo.getEnableGroup();
            if (pluginInfo.getEnableGroup()) {
                Config.enableUser = Boolean.TRUE;
            }
            Config.enableHearBeat = pluginInfo.getEnableHearBeat();
            Config.hearBeatCycle = pluginInfo.getHearBeatCycle();
            Config.enableEventLoop = pluginInfo.getEnableEventLoop();
            Config.eventLoopCycle = pluginInfo.getEventLoopCycle();
            Config.enableCluster = pluginInfo.getEnableCluster();
        }

        // 初始化 uid
        Config.uid = UUID.randomUUID().toString().replaceAll("-", "");

        // 打印
        logger.info(Config.getString());
    }

    /**
     * if Config.enableCluster is true，init message queue。
     */
    public static boolean initMessageQueue() {
        try {
            PulsarInit.init();
        } catch (PulsarClientException | PulsarAdminException e) {
            logger.error("init message queue error", e);
            return false;
        }
        return true;
    }

}

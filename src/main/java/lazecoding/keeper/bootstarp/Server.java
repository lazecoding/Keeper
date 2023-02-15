package lazecoding.keeper.bootstarp;

import lazecoding.keeper.config.Config;
import lazecoding.keeper.config.PluginInfo;
import lazecoding.keeper.config.ServerInfo;
import lazecoding.keeper.util.BeanUtil;
import lazecoding.keeper.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executors;

/**
 * Server 入口
 *
 * @author lazecoding
 */
public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void start() {
        // doStart
        Executors.newSingleThreadExecutor().submit(Bootstrap::doStart);
    }

    public static void init() {
        // init config
        initConfig();
    }

    /**
     * init config
     */
    private static void initConfig() {
        // 初始化 server-config
        ServerInfo serverInfo = BeanUtil.getBean("serverInfo", ServerInfo.class);
        if (!ObjectUtils.isEmpty(serverInfo)) {
            if (StringUtils.hasText(serverInfo.getContextPath())) {
                Config.contextPath = serverInfo.getContextPath();
            }
            if (StringUtils.hasText(serverInfo.getServerPort())) {
                Config.serverPort = serverInfo.getServerPort();
            }
            if (StringUtils.hasText(serverInfo.getHttpObjectLength())) {
                Config.httpObjectLength = serverInfo.getHttpObjectLength();
            }
            if (StringUtils.hasText(serverInfo.getSoBacklog())) {
                Config.soBacklog = serverInfo.getSoBacklog();
            }
        }

        // 初始化 plugin-config
        PluginInfo pluginInfo = BeanUtil.getBean("pluginInfo", PluginInfo.class);
        if (!ObjectUtils.isEmpty(pluginInfo)) {
            Config.enableHearBeat = pluginInfo.getEnableHearBeat();
            Config.hearBeatCycle = pluginInfo.getHearBeatCycle();
            Config.enableResend = pluginInfo.getEnableResend();
            String maxResendTime = pluginInfo.getMaxResendTime();
            if (StringUtils.hasText(maxResendTime)) {
                Config.maxResendTime = Integer.parseInt(maxResendTime);
            }
            String resendCycle = pluginInfo.getResendCycle();
            if (StringUtils.hasText(resendCycle)) {
                Config.resendCycle = Long.parseLong(resendCycle);
            }
        }

        // 初始化 uid
        Config.uid = UUIDUtil.getUUID();

        // 打印
        logger.info("init server config ready.\n{}", Config.getString());
    }

}

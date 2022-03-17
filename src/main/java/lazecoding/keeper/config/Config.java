package lazecoding.keeper.config;

/**
 * 全局配置
 *
 * @author lazecoding
 */
public class Config {

    /**
     * 实例标识
     */
    public static String uid = "";

    // server config

    /**
     * WebSocket Path
     */
    public static String contextPath = "/ws";

    /**
     * 服务绑定的端口号
     */
    public static String serverPort = "12121";

    /**
     * HttpObjectAggregator MaxContentLength
     */
    public static String httpObjectLength = "65536";

    /**
     * TCP 全队列大小
     */
    public static String soBacklog = "1024";

    // plugin config

    /**
     * 是否启用用户模块
     */
    public static boolean enableUser = Boolean.FALSE;

    /**
     * 是否启用用户群组模块
     * <p>
     * 当启用群组模块，默认启用用户模块（enableUser = true）。
     */
    public static boolean enableGroup = Boolean.FALSE;

    /**
     * 是否启用心跳检测模块
     */
    public static boolean enableHearBeat = Boolean.FALSE;

    /**
     * 心跳检测间隔时间，单位: s/秒。
     */
    public static String hearBeatCycle = "15";

    /**
     * 是否启用周期事件处理器
     */
    public static boolean enableEventLoop = Boolean.FALSE;

    /**
     * 周期事件处理器执行间隔时间，单位: s/秒。
     */
    public static String eventLoopCycle = "15";

    /**
     * 是否启用集群
     */
    public static boolean enableCluster = Boolean.FALSE;

    public static String getString() {
        return "ServerConfig {\n" +
                "  uid='" + uid + "'" +
                "\n" +
                "  contextPath='" + contextPath + "'" +
                ", serverPort='" + serverPort + "'" +
                ", httpObjectLength='" + httpObjectLength + "'" +
                ", soBacklog='" + soBacklog + "'" +
                "\n" +
                "  enableUser='" + enableUser + "'" +
                ", enableGroup='" + enableGroup + "'" +
                ", enableHearBeat='" + enableHearBeat + "'" +
                ", hearBeatCycle='" + hearBeatCycle + "'" +
                ", enableEventLoop='" + enableEventLoop + "'" +
                ", eventLoopCycle='" + eventLoopCycle + "'" +
                ", enableCluster='" + enableCluster + "'" +
                "\n}";
    }

}

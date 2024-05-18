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
     * 是否启用心跳检测模块
     */
    public static boolean enableHearBeat = Boolean.FALSE;

    /**
     * 心跳检测间隔时间，单位: s/秒。
     */
    public static String hearBeatCycle = "15";

    /**
     * 是否开启消息重推
     */
    public static Boolean enableResend = Boolean.FALSE;

    /**
     * 重推次数,< 0 代表无限次
     */
    public static int maxResendTime = 3;

    /**
     * 重推间隔 单位/s
     */
    public static long resendCycle = 5L;

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
                ", soBacklog='" + soBacklog + "', " +
                "\n" +
                "  enableHearBeat='" + enableHearBeat + "'" +
                ", hearBeatCycle='" + hearBeatCycle + "'" +
                ", enableCluster='" + enableCluster + "', " +
                "\n" +
                ", enableResend='" + enableResend + "'" +
                ", maxResendTime='" + maxResendTime + "'" +
                ", resendCycle='" + resendCycle + "'" +
                "\n}";
    }

}

package personal.keeper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Plugin 配置
 *
 * @author lazecoding
 */
@Configuration("pluginInfo")
@ConfigurationProperties(prefix = "project.plugin-config")
public class PluginInfo {

    /**
     * 是否启用用户模块
     */
    private Boolean enableUser = Boolean.FALSE;

    /**
     * 是否启用用户群组模块
     * <p>
     * 当启用群组模块，默认启用用户模块（enableUser = true）。
     */
    private Boolean enableGroup = Boolean.FALSE;

    /**
     * 是否启用心跳检测模块
     */
    private Boolean enableHearBeat = Boolean.FALSE;

    /**
     * 心跳检测间隔时间，单位: s/秒。
     */
    private String hearBeatCycle = "";

    /**
     * 是否启用周期事件处理器
     */
    private Boolean enableEventLoop = Boolean.FALSE;

    /**
     * 周期事件处理器执行间隔时间，单位: s/秒。
     */
    private String eventLoopCycle = "";

    /**
     * 是否启用集群
     */
    private Boolean enableCluster = Boolean.FALSE;


    public Boolean getEnableUser() {
        return enableUser;
    }

    public void setEnableUser(Boolean enableUser) {
        this.enableUser = enableUser;
    }

    public Boolean getEnableGroup() {
        return enableGroup;
    }

    public void setEnableGroup(Boolean enableGroup) {
        this.enableGroup = enableGroup;
    }

    public Boolean getEnableHearBeat() {
        return enableHearBeat;
    }

    public void setEnableHearBeat(Boolean enableHearBeat) {
        this.enableHearBeat = enableHearBeat;
    }

    public String getHearBeatCycle() {
        return hearBeatCycle;
    }

    public void setHearBeatCycle(String hearBeatCycle) {
        this.hearBeatCycle = hearBeatCycle;
    }

    public Boolean getEnableEventLoop() {
        return enableEventLoop;
    }

    public void setEnableEventLoop(Boolean enableEventLoop) {
        this.enableEventLoop = enableEventLoop;
    }

    public String getEventLoopCycle() {
        return eventLoopCycle;
    }

    public void setEventLoopCycle(String eventLoopCycle) {
        this.eventLoopCycle = eventLoopCycle;
    }

    public Boolean getEnableCluster() {
        return enableCluster;
    }

    public void setEnableCluster(Boolean enableCluster) {
        this.enableCluster = enableCluster;
    }
}

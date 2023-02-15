package lazecoding.keeper.config;

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
     * 是否启用心跳检测模块
     */
    private Boolean enableHearBeat = Boolean.FALSE;

    /**
     * 心跳检测间隔时间，单位: s/秒。
     */
    private String hearBeatCycle = "";


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

}

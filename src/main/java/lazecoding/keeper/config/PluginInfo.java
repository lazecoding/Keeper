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

    /**
     * 是否开启消息重推
     */
    private Boolean enableResend = Boolean.FALSE;

    /**
     * 重推次数,< 0 代表无限次
     */
    private String maxResendTime = "3";

    /**
     * 重推间隔 单位/s
     */
    private String resendCycle = "5";

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

    public Boolean getEnableResend() {
        return enableResend;
    }

    public void setEnableResend(Boolean enableResend) {
        this.enableResend = enableResend;
    }

    public String getMaxResendTime() {
        return maxResendTime;
    }

    public void setMaxResendTime(String maxResendTime) {
        this.maxResendTime = maxResendTime;
    }

    public String getResendCycle() {
        return resendCycle;
    }

    public void setResendCycle(String resendCycle) {
        this.resendCycle = resendCycle;
    }
}

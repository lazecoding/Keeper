package lazecoding.keeper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * WebSocket 服务器配置
 *
 * @author lazecoding
 */
@Configuration("serverInfo")
@ConfigurationProperties(prefix = "project.server-config")
public class ServerInfo {

    /**
     * WebSocket Path
     */
    private String contextPath = "";

    /**
     * 服务绑定的端口号
     */
    private String serverPort = "";

    /**
     * HttpObjectAggregator MaxContentLength
     */
    private String httpObjectLength = "";

    /**
     * TCP 全队列大小
     */
    private String soBacklog = "";

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getHttpObjectLength() {
        return httpObjectLength;
    }

    public void setHttpObjectLength(String httpObjectLength) {
        this.httpObjectLength = httpObjectLength;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getSoBacklog() {
        return soBacklog;
    }

    public void setSoBacklog(String soBacklog) {
        this.soBacklog = soBacklog;
    }

}


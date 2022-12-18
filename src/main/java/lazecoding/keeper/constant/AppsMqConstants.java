package lazecoding.keeper.constant;

import lazecoding.keeper.exception.NilParamException;
import org.springframework.util.StringUtils;

/**
 * App 消费消息 MQ 常量
 *
 * @author lazecoding
 */
public class AppsMqConstants {

    /**
     * 交换机
     */
    private static String exchange = "lazecoding.keeper.app.websocket.message.cost";

    /**
     * 路由
     */
    private static String route = "lazecoding-keeper-route-app-websocket-message-cost-";

    /**
     * 队列
     */
    private static String queue = "lazecoding-keeper-queue-app-websocket-message-cost-";

    public static String exchange() {
        return exchange;
    }

    public static String route(String app) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        return route + app;
    }

    public static String queue(String app) {
        if (!StringUtils.hasText(app)) {
            throw new NilParamException("app is nil.");
        }
        return queue + app;
    }
}

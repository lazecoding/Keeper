package lazecoding.keeper.constant;

/**
 * MQ 常量
 *
 * @author lazecoding
 */
public enum MqConstants {

        /**
         * WebSocket Server 同步消息
         */
        WEBSOCKET_MESSAGE("lazecoding.keeper.websocket.message", "lazecoding-keeper-route--websocket-message", "lazecoding-keeper-queue-websocket-message-",
                "lazecoding-keeper-websocket-message", "WebSocket Server 同步消息");

        /**
         * 交换机
         */
        private String exchange;

        /**
         * 路由
         */
        private String route;

        /**
         * 队列
         */
        private String queue;

        /**
         * 别名
         */
        private String alias;

        /**
         * 描述
         */
        private String description;

        public String getExchange() {
                return exchange;
        }

        public String getRoute() {
                return route;
        }

        public String getQueue() {
                return queue;
        }

        public String getAlias() {
                return alias;
        }

        public String getDescription() {
                return description;
        }

        MqConstants(String exchange, String route, String queue, String alias, String description) {
                this.exchange = exchange;
                this.route = route;
                this.queue = queue;
                this.alias = alias;
                this.description = description;
        }
}

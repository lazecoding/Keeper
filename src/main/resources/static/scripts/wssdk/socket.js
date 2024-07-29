/**
 * WebSocket客户端
 *
 * 构造函数
 * {
 *    url: url,                // ws 地址
 *    reconnectInterval: 1000, // 重连间隔
 *    heartbeat: true,         // 是否开启心跳
 *    heartbeatInterval: 3000, // 心跳间隔
 *    heartbeatMessage: 'I'    // 心跳消息
 * }
 * constructor                           构造函数，建立链接
 * WebSocketClient.isActive()            判断链接是否可用
 * WebSocketClient.close()               断开链接(标志着这个实例链接生命周期结束)
 * WebSocketClient.onMessage(function)   注册消息接收事件处理函数
 * WebSocketClient.onOpen(function)      注册链接建立事件处理函数
 * WebSocketClient.onClose(function)     注册链接关闭事件处理函数
 * WebSocketClient.onError(function)     注册链接异常事件处理函数
 * WebSocketClient.sendMessage(message)  发送消息
 */
class WebSocketClient {
  constructor(config) {
    const defaultConfig = {
      url: null,
      reconnectInterval: 3000,
      heartbeat: false,
      heartbeatInterval: 60000,
      heartbeatMessage: "I",
    };
    this.finalConfig = { ...defaultConfig, ...config };
    console.log("finalConfig:", this.finalConfig);
    this.url = this.finalConfig.url; // ws 地址
    if (!this.url) return; // 如果地址没配置，直接返回 null
    this.reconnectInterval = this.finalConfig.reconnectInterval; // 重连间隔
    this.heartbeat = this.finalConfig.heartbeat; // 是否发送心跳
    this.heartbeatInterval = this.finalConfig.heartbeatInterval; // 心跳间隔
    this.heartbeatMessage = this.finalConfig.heartbeatMessage; // 心跳消息
    this.uid = null; // 链接唯一标识
    this.ws = null; // WebSocket 对象
    this.reconnecting = false; // 是否重连中
    this.lastReconnectTime = null; // 上一次重连时间
    this.heartbeatTask = null; // 心跳任务
    this.messageHandlers = []; // 存储所有注册的消息处理函数
    this.openHandlers = []; // 存储所有注册的链接建立处理函数
    this.closeHandlers = []; // 存储所有注册的链接关闭处理函数
    this.errorHandlers = []; // 存储所有注册的链接异常处理函数
    // 链接断开后行为 (默认重连，控制 reconnectInterval 重连间隔，和上一次重连之间大于 reconnectInterval 则直接重连)
    this.closeAfter = () => {
      if (this.reconnecting) return;
      this.reconnecting = true;
      // 获取当前时间
      const nowTimeMs = new Date().getTime();
      // 获取可以重连的时间
      const canReconnectTimeMs = this.lastReconnectTime
        ? this.lastReconnectTime.getTime() + this.reconnectInterval
        : 0;

      // 如果当前时间大于等于可重连时间，直接重连
      if (nowTimeMs >= canReconnectTimeMs) {
        console.log("WebSocket Reconnect Start. time:", new Date());
        this.lastReconnectTime = new Date();
        this.init();
        this.reconnecting = false;
        console.log("WebSocket Reconnect End. time:", new Date());
      } else {
        // 当前时间未到可重连时间，延时重连
        const reconnectIntervalTime = canReconnectTimeMs - nowTimeMs;
        setTimeout(() => {
          console.log("WebSocket Reconnect Start. time:", new Date());
          this.lastReconnectTime = new Date();
          this.init();
          this.reconnecting = false;
          console.log("WebSocket Reconnect End. time:", new Date());
        }, reconnectIntervalTime);
      }
    };
    this.uid = this.getUid(); // uid
    this.init(); // 初始化
    console.log(this);
  }

  // 获取唯一标识 uid
  getUid() {
    return Date.now().toString(36) + Math.random().toString(36);
  }

  // 链接是否处理活跃状态
  isActive() {
    return this.ws != undefined && this.ws != null && this.ws.readyState == WebSocket.OPEN;
  }

  // 初始化 WebSocketClient
  init() {
    if (!window.WebSocket) {
      console.error("This Browser Does Not Support WebSocket.");
      return;
    }

    // 如果链接已经开启,而且可用,不创建新的连接
    if (this.isActive()) {
      console.error(
        "The Link Already Exists. Duplicate Creation Is Prohibited."
      );
      return;
    }

    // ws 链接
    this.ws = new WebSocket(this.url);

    // 链接建立事件
    this.ws.onopen = (event) => {
      console.log("WebSocket Connection Opened. time:", new Date());
      this.doHandlers(event, this.openHandlers); // 调用注册的处理函数
      if (this.heartbeat) {
        // 创建心跳线程
        this.registerHeartbeatTask();
      }
    };
    // 消息接收事件
    this.ws.onmessage = (event) => {
      this.doHandlers(event, this.messageHandlers); // 调用注册的处理函数
    };
    // 链接关闭事件
    this.ws.onclose = (event) => {
      console.log("WebSocket Connection Closed. time:", new Date());
      this.doHandlers(event, this.closeHandlers); // 调用注册的处理函数
      this.cancelHeartbeatTask();
      if (!this.reconnecting) {
        this.closeAfter();
      }
    };
    // 链接异常事件
    this.ws.onerror = (error) => {
      console.log("WebSocket Connection Error. time:", new Date());
      this.doHandlers(error, this.openHandlers); // 调用注册的处理函数
      this.cancelHeartbeatTask();
      this.closeAfter();
    };
  }

  // 关闭链接
  close() {
    //如果浏览器支持WebSocket
    if (window.WebSocket) {
      if (this.ws) {
        // 断开链接的时候要重写 closeAfter，避免 onclose 时重连
        this.closeAfter = () => {
          console.log("WebSocket Real Closed.");
        };
        this.ws.close();
      }
      console.log("WebSocket Closed. time:", new Date());
    } else {
      console.error("This Browser Does Not Support WebSocket.");
    }
  }

  // 发送数据
  sendMessage(message) {
    if (!window.WebSocket) {
      console.error("This Browser Does Not Support WebSocket.");
      return;
    }
    //当 WebSocket 可用
    if (this.isActive()) {
      this.ws.send(message);
    } else {
      console.error("WebSocket Is Not Open. Message Can Not Sent");
    }
  }

  // 消息事件
  onMessage(handler) {
    this.registerHandler(handler, this.messageHandlers);
  }

  // 链接建立事件
  onOpen(handler) {
    this.registerHandler(handler, this.openHandlers);
  }

  // 链接关闭事件
  onClose(handler) {
    this.registerHandler(handler, this.closeHandlers);
  }

  // 异常事件
  onError(handler) {
    this.registerHandler(handler, this.errorHandlers);
  }

  // 注册事件处理函数
  registerHandler(handler, handlers) {
    if (typeof handler === "function") {
      handlers.push(handler);
    }
  }

  // 处理注册的处理函数
  doHandlers(event, handlers) {
    for (const handler of handlers) {
      handler(event);
    }
  }

  // 注册心跳任务
  registerHeartbeatTask() {
    console.log("Heartbeat Task Register Start. uid:", this.uid, " time:", new Date());
    // 如果存在，先销毁
    if (this.heartbeatTask) {
      this.heartbeatTask.terminate();
      this.heartbeatTask = null;
    }
    let taskType = "heartbeat";
    const url = "task_executor.js";
    this.heartbeatTask = new Worker(url);
    // 注册 task
    this.heartbeatTask.postMessage({
      uid: this.uid, // 标识任务唯一性
      cycle: this.heartbeat, // 是否开启周期
      cycleInterval: this.heartbeatInterval, // 周期间隔时间
      type: taskType, // 事件类型
    });
    // 回调监听
    this.heartbeatTask.onmessage = (event) => {
      console.log("Heartbeat Task On Message. time:", new Date(), " event:", event);
      const task = event.data;
      if (task && task.type && task.type === taskType) {
        this.sendMessage(this.heartbeatMessage);
      }
    };
    console.log("Heartbeat Task Register End. uid:", this.uid, " time:", new Date());
  }

  // 注销心跳任务
  cancelHeartbeatTask() {
    console.log("Cancel Heartbeat Task. uid:", this.uid, " time:", new Date());
    if (this.heartbeatTask) {
      this.heartbeatTask.terminate();
      this.heartbeatTask = null;
    }
  }
}

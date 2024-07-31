// shared_socket_in_xxx.js
var connections = [];
var socket;

// 常量
// WebSocket 地址
// const url = "ws://work.hanweb.com/websocket-common-api/ws?U=hanweb";
const url = "ws://127.0.0.1:9966/ws?U=hanweb";
// 是否开启心跳
const heartbeat = true;
// 心跳间隔
const cycleInterval = 5000;
// 心跳内容
const heartbeatMessage = "I";
// 心跳事件
var heartbeatInterval;
// 是否重连中
var reconnecting = false;
// 重连间隔
const reconnectInterval = 5000;
// 上一次重连事件
var lastReconnectTime;

// 初始化 WebSocket
function initWebSocket() {
  // 初始化链接
  socket = new WebSocket(url);
  console.log('链接初始化');
  socket.onopen = function (event) {
    // 开始心跳
    startInterval();
    // 广播
    broadcast("WebSocket connection opened");
  };

  socket.onmessage = function (event) {
    console.log("Message from server:", event.data);
    broadcast(event.data);
  };

  socket.onclose = function (event) {
    // 中止心跳
    stopInterval();
    // 广播
    broadcast("WebSocket connection closed");
    // 重连
    reconnect();
  };

  socket.onerror = function (error) {
    // 中止心跳
    stopInterval();
    // 广播
    broadcast("WebSocket error: " + error);
    // 重连
    reconnect();
  };
}

// 开始心跳
function startInterval() {
  if (heartbeat && cycleInterval > 0) {
    heartbeatInterval = setInterval(() => {
      if (isActive) {
        socket.send(heartbeatMessage);
      }
    }, cycleInterval);
  }
}

// 停止心跳
function stopInterval() {
  if (heartbeatInterval) {
    clearInterval(heartbeatInterval);
    heartbeatInterval = null; // Reset the heartbeatInterval to indicate it's stopped
  }
}

// 链接是否处理活跃状态
function isActive() {
  return socket && socket.readyState == WebSocket.OPEN;
}

// 重连
function reconnect() {
  if (reconnecting) return;
  reconnecting = true;
  // 获取当前时间
  const nowTimeMs = new Date().getTime();
  // 获取可以重连的时间
  const canReconnectTimeMs = lastReconnectTime
    ? lastReconnectTime.getTime() + reconnectInterval
    : 0;

  // 如果当前时间大于等于可重连时间，直接重连
  if (nowTimeMs >= canReconnectTimeMs) {
    console.log("WebSocket Reconnect Start. time:", new Date());
    lastReconnectTime = new Date();
    initWebSocket();
    reconnecting = false;
    console.log("WebSocket Reconnect End. time:", new Date());
  } else {
    // 当前时间未到可重连时间，延时重连
    const reconnectIntervalTime = canReconnectTimeMs - nowTimeMs;
    setTimeout(() => {
      console.log("WebSocket Reconnect Start. time:", new Date());
      lastReconnectTime = new Date();
      initWebSocket();
      reconnecting = false;
      console.log("WebSocket Reconnect End. time:", new Date());
    }, reconnectIntervalTime);
  }
}

// 广播消息
function broadcast(message) {
  connections.forEach(function (connection) {
    connection.postMessage(message);
  });
}

onconnect = function (event) {
  var port = event.ports[0];
  connections.push(port);

  // 用来接受来自 Web 的消息，转发给 WebSocket
  port.onmessage = function (event) {
    if (isActive()) {
      socket.send(event.data);
    } else {
      console.log("WebSocket is not open. Cannot send message.");
    }
  };

  // 没啥意义
  port.onclose = function () {
    connections = connections.filter(function (connection) {
      return connection !== port;
    });
  };

  // Initialize WebSocket if it is the first connection
  if (!isActive()) {
    initWebSocket();
  }

  port.postMessage("Connected to SharedWorker");
};

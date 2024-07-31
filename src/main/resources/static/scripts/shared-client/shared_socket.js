// shared_socket.js
if (typeof SharedWorker !== "undefined") {
  var worker = new SharedWorker("shared_socket_in_xxx.js");

  // 页面关闭或刷新时通知 worker
  window.addEventListener("beforeunload", function () {
    // 向 worker 发送关闭消息
    worker.port.close();
  });

  // 监听 worker 消息
  worker.port.onmessage = function (event) {
    console.log("Received from worker:", event.data);
    var messagesDiv = document.getElementById("messages");
    var newMessage = document.createElement("div");
    newMessage.textContent = event.data;
    messagesDiv.appendChild(newMessage);
  };

  // 启动
  worker.port.start();

  // 发送消息
  function sendMessage() {
    var input = document.getElementById("messageInput");
    worker.port.postMessage(input.value);
    input.value = "";
  }

  document.getElementById("sendButton").onclick = sendMessage;
} else {
  console.log("Your browser does not support SharedWorker.");
}

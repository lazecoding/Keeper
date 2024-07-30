/**
 * 以事件驱动的思想设计，在调用方设定的执行时间要求响应调用方
 * 
 * const task = {
 *   uid: '',               // 标识任务唯一性
 *   cycle: true,           // 是否开启周期
 *   cycleInterval: 3000,   // 周期间隔时间
 *   delay: 3000,           // 延时时间（如果不是周期时间，则通过该字段判断是否是延时请求）
 *   type: ''              // 事件类型
 * }
 */
// 监听主线程事件
self.onmessage = (event) => {
  console.log("Task Executor Register Event:", event);
  const task = event.data;
  if (!task || !task.uid) {
    return;
  }
  // 是否是周期事件
  if (task.cycle && task.cycleInterval && task.cycleInterval > 0) {
    const heartbeatInterval = setInterval(() => {
      self.postMessage(task);
    }, task.cycleInterval);
  } else {
    if (task.delay && task.delay > 0) {
      setTimeout(() => {
        self.postMessage(task);
      }, this.reconnectInterval);
    } else {
      self.postMessage(task);
    }
  }
};

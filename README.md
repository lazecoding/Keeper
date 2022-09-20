# Keeper release-2.0.0

待施工...........

分布式 WebSocket 服务器。

### 架构

本项目基于 Netty 开发，继承 Netty 架构并进一步扩展。

特点：

- Boss 线程、IO 线程、业务线程分离。

### 插件

本服务功能插件化。

#### 心跳检测

`project.plugin-config.enableHearBeat` 属性决定是否启用 `心跳检测` 模块，`project.plugin-config.hearBeatCycle` 控制心跳周期间隔时长，单位 s/秒。

```yaml
project:
  plugin-config:
    enableHearBeat: false
    hearBeatCycle: 15
```

#### 事件周期处理

`project.plugin-config.enableEventLoop` 属性决定是否启用 `事件周期处理` 模块，`project.plugin-config.eventLoopCycle` 控制事件处理周期间隔时长，单位 s/秒。

```yaml
project:
  plugin-config:
    enableEventLoop: false
    eventLoopCycle: 15
```

### 环境配置需求

#### ulimit -n

Linux 对每个进程打开的文件句柄数量做了限制，如果超出：报错 "Too many open file"。

需要调大，如 65535，根据应用实际情况进一步调整。

#### 堆内存

指定最小和最大堆大小，而且为了防止垃圾收集器在最小、最大之间收缩堆而产生额外的时间，通常把最大、最小设置为相同的值。

```java
-Xms12G -Xmx12G
```

####  堆外内存

DirectByteBuffer 对象的回收需要依赖 Old GC 或者 Full GC 才能触发清理,我们需要通过 JVM 参数
`-XX:MaxDirectMemorySize` 指定堆外内存的上限大小，当堆外内存的大小超过该阈值时， 就会触发一次 Full GC 进行清理回收。

```java
-XX:MaxDirectMemorySize=4G
```

一定要配置：-Xms、-Xmx 、-XX:MaxDirectMemorySize，它们的和不能超过 docker 的最大内存，否则当 docker 内存占满了会被 OOM kill。

#### G1

前台应用推荐使用 G1，G1 比 CMS 更适合大内存的垃圾收集。

```C
-XX:+UseG1GC
```

> 使用 G1 时，不要使用 -Xmn、-XX:NewRatio 等其他相关显式设置年轻代大小的参数，它们会覆盖暂停时间的指标。

### 部署

#### Nginx 代理

```C
location / {
    proxy_pass http://webscoket;
    proxy_set_header Host $host:$server_port;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
}
```

用 Nginx 反向代理某个业务，发现平均 1 分钟左右，就会出现 WebSocket 连接中断。
产生原因：Nginx 等待第一次通讯和第二次通讯的时间差，超过了它设定的最大等待时间，简单来说就是超时！

解决方法 1：

其实只要配置 nginx.conf 的对应 localhost 里面的这几个参数就好。

- proxy_connect_timeout;
- proxy_read_timeout;
- proxy_send_timeout;

```C
http {
    server {
        location / {
            root   html;
            index  index.html index.htm;
            proxy_pass http://webscoket;
            proxy_http_version 1.1;
            proxy_connect_timeout 4s;                #配置点1
            proxy_read_timeout 60s;                  #配置点2，如果没效，可以考虑这个时间配置长一点
            proxy_send_timeout 12s;                  #配置点3
            proxy_set_header Upgrade $http_upgrade; 
            proxy_set_header Connection "Upgrade";  
        }
    }
}
```

解决方法 2：

发心跳包，原理就是在有效地再读时间内进行通讯，重新刷新再读时间

`proxy_read_timeout` 这个参数是服务器对你等待最大的时间，也就是说当你 WebSocket 使用 Nginx 转发的时候，`proxy_read_timeout` 时间内没有通讯，
依然是会断开的，设置时间内有心跳或者有通信的话，是可以保持连接不中断的。


## License

Keeper software is licenced under the Apache License Version 2.0. See the [LICENSE](https://github.com/lazecoding/Keeper/blob/main/LICENSE) file for details.
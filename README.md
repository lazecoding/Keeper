# Keeper

分布式 WebSocket 服务器

### 注意事项

- 心跳检测周期要小于消息推送周期。
- IO 线程和业务线程分离：对于小业务，依旧放到 worker 线程中处理，对于需要和中间件交互的丢到业务线程池处理，避免 worker 阻塞。
- WebSocket 握手阶段支持参数列表。

### 容器

- CHANNEL_GROUP：链接标识 和 链接上下文对象 的映射。

### 组件

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

一定要配置：-Xms、-Xmx 、-XX:MaxDirectMemorySize，它们的和不能超过 docker 的最大内存，否则当 docker 内存占满了会被 oom kill。

#### G1

前台应用推荐使用 G1，G1 比 CMS 更适合大内存的垃圾收集。

```C
-XX:+UseG1GC
```

> 使用 G1 时，不要使用 -Xmn、-XX:NewRatio 等其他相关显式设置年轻代大小的参数，它们会覆盖暂停时间的指标。
package personal.keeper.bootstarp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.keeper.config.Config;
import personal.keeper.hander.DefaultSessionHandler;
import personal.keeper.hander.IncomingRequestHandler;
import personal.keeper.plugins.eventloop.EventLoop;
import personal.keeper.plugins.hearbeat.DefaultHearBeatHandler;

/**
 * Internal startup code.
 *
 * @author lazecoding
 */
public class Bootstrap {

    private final static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    /**
     * 服务器启动
     */
    public static void doStart() {

        // boos 1 个线程即可
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        // 默认值：核心数两倍
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            // TCP 全连接队列大小
            serverBootstrap.option(ChannelOption.SO_BACKLOG, Integer.parseInt(Config.soBacklog));
            // 允许重复使用本地地址和端口
            serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
            // ALLOCATOR 参数，设置 SocketChannel 中分配的 ByteBuf 类型
            // 第二个参数需要传入一个 ByteBufAllocator，用于指定生成的 ByteBuf 的类型
            // new PooledByteBufAllocator(true) 表示池化并使用直接内存
            serverBootstrap.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true));
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true));
            // 保持长连接
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 不延迟发送
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    // WebSocket 协议本身是基于 HTTP 协议的，所以这边也要使用 HTTP 解编码器
                    ch.pipeline().addLast("http-codec", new HttpServerCodec());
                    // Netty 是基于分段请求的，HttpObjectAggregator 的作用是将请求分段再聚合,参数是聚合字节的最大长度
                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(Integer.parseInt(Config.httpObjectLength)));
                    // 以块的方式来写的处理器,方便大文件传输
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    // WebSocket 数据压缩扩展
                    ch.pipeline().addLast(new WebSocketServerCompressionHandler());

                    if (Config.enableHearBeat) {
                        // 增加心跳事件支持 > 第一个参数：读空闲；第二个参数：写空闲；第三个参数：读写空闲。
                        ch.pipeline().addLast(new IdleStateHandler(
                                Integer.parseInt(Config.hearBeatCycle),
                                Integer.parseInt(Config.hearBeatCycle),
                                Integer.parseInt(Config.hearBeatCycle)));
                        ch.pipeline().addLast(new DefaultHearBeatHandler());
                    }

                    // 会话处理器
                    ch.pipeline().addLast(new DefaultSessionHandler());

                    // WebSocket 握手、控制帧处理
                    ch.pipeline().addLast(new WebSocketServerProtocolHandler(Config.contextPath, null, true));

                    // 入站请求
                    ch.pipeline().addLast(new IncomingRequestHandler());
                }
            });
            Channel channel = serverBootstrap.bind(Integer.parseInt(Config.serverPort)).sync().channel();
            logger.info("Connection is opened ... port:{}  contextPath:{}", Config.serverPort, Config.contextPath);

            // 注册周期事件处理器
            if (Config.enableEventLoop) {
                EventLoop.doRegister();
            }

            // Wait until the connection is closed.
            channel.closeFuture().sync();
            logger.info("Connection is closed.");
        } catch (InterruptedException e) {
            logger.error("Bootstrap doStart Exception:{}.", e.getCause().toString());
            throw new Error("Server Disconnected");
        } finally {
            // 服务器关闭后，释放资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            if (Config.enableEventLoop) {
                EventLoop.cancel();
            }
        }
    }

}

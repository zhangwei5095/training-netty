package com.shangpin.netty.client.chapter04.linebased;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 时间服务器客户端<br>
 * Netty客户端开发相比与服务器更简单。<br>
 * 
 * <pre>
 * 
 * </pre>
 * 
 * 
 * @author percy
 *
 */
public class TimeClient {

	public void connect(int port, String host) throws Exception {
		// 配置客户端NIO线程组
		// 创建客户端处理的I/O读写的NioEventLoopGroup线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 然后继续创建客户端辅助启动类Bootstrap,随后需要其起进行配置。
			Bootstrap b = new Bootstrap();
			// 与服务器不同的是，它的Channel需要配置NioSocketChannel，然后为其添加Handler
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						// 此处为了简单直接创建匿名内部类来实现initChannel方法，其作用是当创建NioSocketChannel成功后
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							// 直接添加两个解码器
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new TimeClientHandler());
						}

					});
			// 发起异步连接操作
			ChannelFuture f = b.connect(host, port).sync();
			// 等待客户端链路关闭 
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放NIO线程组
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}

		// 客户端启动辅助类设置完成之后，调用connect方法发起异步连接，然后调用同步方法等待连接成功。
		// 最后，当客户端连接关闭后，客户端主函数退出，在退出之前，释放NIO线程组的资源。
		new TimeClient().connect(port, "127.0.0.1");
	}

}

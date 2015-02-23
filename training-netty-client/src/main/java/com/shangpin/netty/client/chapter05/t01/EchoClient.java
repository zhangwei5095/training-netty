package com.shangpin.netty.client.chapter05.t01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {

	public void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							// 首先创建分隔符缓冲对象ByteBuf，本例中使用"$_"作为分隔符
							ByteBuf delimiter = Unpooled.copiedBuffer("$_"
									.getBytes());
							// 创建DelimiterBasedFrameDecoder对象，将其加入到ChannelPipeLine中。
							// 它有多个构造方法，这里我们传递两个参数，第一个参数表示单条消息的最大长度，当达到该长度后仍然美柚查到分隔符，就抛出TooLongFrameException异常。
							// 防止由于异常码缺失分隔符盗跖的内存溢出，这是Netty解码器的可靠性保护；
							// 第二个参数就是分隔符缓冲对象。
							ch.pipeline().addLast(
									new DelimiterBasedFrameDecoder(1024,
											delimiter));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new EchoClientHandler());
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
		new EchoClient().connect(port, "127.0.0.1");
	}

}

package com.shangpin.netty.client.chapter07;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SubRequestClient {
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

							// 我们禁止了对类加载器进行缓存，它在基于OSGi的动态模块化编程中经常使用
							// 由于OSGi对bundle可以进行热部署和热升级，当某个bundle升级后，它对应的类加载器也将一起升级，
							// 因此在动态模块化编程过程中，很少对类加载器进行缓存，因为它随时可能会发生变化。
							ch.pipeline().addLast(
									new ObjectDecoder(1024 * 1024,
											ClassResolvers.cacheDisabled(this
													.getClass()
													.getClassLoader())));

							// 这里增加了一个ObjectEncoder，它可以在消息发送的使用自动实现Serializable的POJO对象进行编码，因此用户无须亲自对对象手工序列化
							// 只需要关注自己 的业务逻辑处理即可，对象序列化和反序列化都由Netty的对象编码器搞定的。
							ch.pipeline().addLast(new ObjectEncoder());
							// 进行业务逻辑处理
							ch.pipeline()
									.addLast(new SubRequestClientHandler());
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
		new SubRequestClient().connect(port, "127.0.0.1");
	}
}

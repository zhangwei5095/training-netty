package com.shangpin.netty.chapter04.linebased;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 解决粘包问题<br>
 * 
 * 使用LineBasedFrameDecoder来解决
 * 
 * @author percy
 *
 */
public class TimeServer {

	public void bind(int port) throws InterruptedException {
		// 创建两个线程组，专门用于网络事件处理。实际上就是Reactor线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// Netty启动NIO服务器端启动类，目的降低服务端开发的复杂度
			ServerBootstrap bootStrap = new ServerBootstrap();
			// 将两个NIO线程组当作入参传递到ServerBootstrap当中
			bootStrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			ChannelFuture future = bootStrap.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			// 优雅退出， 释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}
		new TimeServer().bind(port);
	}
}

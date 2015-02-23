package com.shangpin.netty.chapter10.t01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 校验成功可以链接方式打开当前文件目录，每个目录都是一个超链接，可以递归访问。
 * 
 * @author percy
 *
 */
public class HttpFileServer {

	private static final String DEFAULT_URL = "/src/com/shangpin/netty";

	public void run(final int port, final String url) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChildChannelHandler(url));
			// 绑定端口，同步等待成功
			ChannelFuture future = bootStrap.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			// 优雅退出， 释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
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
		String url = DEFAULT_URL;
		if (args.length > 1) {
			url = args[1];
		}
		new HttpFileServer().run(port, url);
	}

}

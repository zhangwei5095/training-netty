package com.shangpin.netty.client.chapter12;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * UDP客户端开发<br>
 * 
 * <pre>
 * UDP程序的客户端和服务端代码非常类似，唯一不同之处是UDP客户端会主动
 * 构造请求消息，向本网段内的所有主机广播请求消息，对于服务器而言，接收到广播请求消息
 * 之后会向广播消息的发起方进行定点发送。
 * 
 * 下面看一下UDP客户端的实现
 * 
 * 
 * </pre>
 * 
 * @author percy
 *
 */
public class ChineseProverbClient {

	public void run(int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			// 由于使用UDP通信，在创建Channel的时候需要通过NioDatagramChannel来创建，
			// 随后设置Socket参数支持广播，最后设置业务处理的Handler

			// 相对于TCP通信，UDP不存在客户端和服务端的实际连接，因此不需要为连接（ChannelPipeline）
			// 设置handler,对于服务端，
			// 只需要设置启动辅助类的handle即可。

			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST, true)
					.handler(new ChineseProverbClientHandler());

			Channel ch = b.bind(0).sync().channel();
			// 向网段内的所有机器广播UDP消息
			// 创建UDP Channel和设置的支持广播属性等与服务器完全一致。由于不需要和服务器建立链路
			// UDP Channel创建完成后，客户端就要主动发送广播消息：TCP客户端是在客户端和服务端链路建立成功之后由
			// 客户端的业务handler发送消息，这就是两者最大的区别。
			ch.writeAndFlush(
					new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询？",
							CharsetUtil.UTF_8), new InetSocketAddress(
							"255.255.255.255", port))).sync();
			// 用于构造DatagramPacket发送广播消息，注意，广播消息的IP设置为"255.255.255.255"。
			// 消息广播后，客户端等待15S用于接收服务端的应答消息，然后退出并释放资源.
			if (!ch.closeFuture().await(15000)) {
				System.out.println("查询超时！");
			}

		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		new ChineseProverbClient().run(port);
	}

}

package com.shangpin.netty.chapter12;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * UDP协议服务端开发<br>
 * <ul>
 * <li>UDP协议简介
 * 
 * <pre>
 * UDP是无连接的，通信双方不需要简历链路连接。在网络中它用于处理数据包，
 * 在OSI模型中，它处于第四层传输层，即位于IP协议的上一层。它不对数据包分组、组装、校验和排序，因此是不可靠的。
 * 报文的发送者不知道报文释放被对方正确接收。
 * 
 * UDP数据包格式有首部和数据两个部分，首部很简单，为8个字节，包括以下部分。
 * 1、源端口：源端口号，2个字节，最大值为65535；
 * 2、目的端口：目的端口，2个字节，最大值为65535；
 * 3、长度：2字节，UDP用户数据报的总长度；
 * 4、校验和：2字节，用于校验UDP数据报的数字段和包含UDP数据报首部的“伪首部”。
 * 		其校验方法类似于IP分组首部中的首部校验和。
 * 
 * 伪首部，又称伪包头(Pseudo Header):是指在TCP的分段和UDP的数据报格式中，
 * 在数据报首部前面增加源IP地址、目的IP地址、IP分组的协议字段、TCP或UDP数据报的总长度，共12字节，
 * 所构成的扩展首部结构。此伪首部是一个临时的结构，它既不向上也不向下传递，仅仅是为了可以校验套接字的正确性。
 * 
 * 
 * 4						4					1		1			2
 * 源IP地址	目的IP地址	0	17	UDP报文长度
 * 伪首部	源端口	目的端口	～～～	校验和
 * 				首部		UDP	UDP数据
 * IP分组	IP分组
 * 
 * 
 * UDP协议的特点如下。
 * 1、UDP传输数据前并不与对方建立连接，即UDP是无连接的。在传输数据前，发送
 * 		方和接收方相互交换信息使双方同步。
 * 2、UDP对接收到的数据报不发送确认信号，发送端不知道数据是否被正确接收，也不会重发数据。
 * 3、UDP传送数据比TCP快速，系统开销少：UDP比较简单，UDP头包含了源端口、目的端口、消息长度
 * 		和校验和等很少的字节。由于UDP比TCP简单、灵活，常用于可靠性要求不高的数据传输，如视频、图片以及简单文件
 * 		传输系统(TFTP)等。TCP则适用于可靠性要求很高但实时性要求不高的应用，如文件传输协议FTP、HTTP、SMTP等。
 * 
 * 
 * 
 * </pre>
 * 
 * <li>UDP协议服务端开发
 * 
 * <pre>
 * 由于UDP通信双方不需要建立链路，所以，代码相对于TCP更加简单一些，下面来看服务端代码
 * </pre>
 * 
 * </ul>
 * 
 * 
 * @author percy
 *
 */
public class ChineseProverbServer {

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
					.handler(new ChineseProverbServerHandler());
			b.bind(port).sync().channel().closeFuture().await();
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
		new ChineseProverbServer().run(port);
	}

}

package com.shangpin.netty.chapter05.t01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {

	int counter = 0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// 直接将接受的消息打印出来，由于DelimiterBasedFrameDecoder自动对请求消息进行了解码，
		// 后续的ChannelHandler接收到的msg对象就是一个完整的消息包。
		// 第二个ChannelHandler是StringDecoder，它将ByteBuf解码成字符串对象；
		// 第三个EchoServerHandler接收到的msg消息就是解码后的字符串对象。
		String body = (String) msg;
		System.out.println("This is " + ++counter + " times receive client : ["
				+ body + " ]");
		body += "$_";
		ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(echo);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();// 发生异常，关闭链路
	}
}

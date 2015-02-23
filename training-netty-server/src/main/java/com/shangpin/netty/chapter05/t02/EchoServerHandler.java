package com.shangpin.netty.chapter05.t02;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {

	int counter = 0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String body = (String) msg;
		System.out.println("This is " + ++counter + " times receive client : ["
				+ body + " ]");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();// 发生异常，关闭链路
	}
}

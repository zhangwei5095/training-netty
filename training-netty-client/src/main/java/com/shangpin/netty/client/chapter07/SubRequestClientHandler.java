package com.shangpin.netty.client.chapter07;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import com.shangpin.netty.chapter07.SubscribeRequest;

public class SubRequestClientHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 在链路激活的时候循环构造10条订购请求消息，最后一次性发送给服务端。
		for (int i = 0; i < 10; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}

	private SubscribeRequest subReq(int i) {
		SubscribeRequest request = new SubscribeRequest();
		request.setAddress("南京市江宁区方山国家地址公园");
		request.setMobile("13522290680");
		request.setProductName("Netty 权威指南");
		request.setUsername("Percy");
		request.setRequestId(i);
		return request;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("Receive server response : [ " + msg + " ]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

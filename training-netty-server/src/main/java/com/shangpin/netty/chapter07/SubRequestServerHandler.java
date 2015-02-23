package com.shangpin.netty.chapter07;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubRequestServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// 经过解码器Handler ObjectDecoder的解码，SubRequestSeverHandler接收到请求消息已经
		// 被自动解码为SubscribeRequest对象，可以直接使用。
		SubscribeRequest request = (SubscribeRequest) msg;
		if ("Percy".equals(request.getUsername())) {
			System.out.println("Service accept client subscribe request : ["
					+ request.toString());
			ctx.writeAndFlush(resp(request.getRequestId()));
		}
	}

	private Object resp(int requestId) {
		SubscribeResponse response = new SubscribeResponse();
		response.setCode(0);
		response.setResponseId(requestId);
		response.setDesc("Netty book order succeed,  3 days later, sent to th designated address...");
		return response;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

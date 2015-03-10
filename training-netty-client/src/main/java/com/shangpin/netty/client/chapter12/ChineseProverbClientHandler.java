package com.shangpin.netty.client.chapter12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ChineseProverbClientHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		String response = msg.content().toString(CharsetUtil.UTF_8);
		if (response.startsWith("谚语查询结果：")) {
			System.out.println(response);
			// 接收到服务端消息之后将其转换为字符串，然后判断释放以“谚语查询结果：”开头，如果没有发生丢包等问题，数据是完整的，就
			// 打印查询结果，然后释放资源。
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		cause.printStackTrace();
	}

}

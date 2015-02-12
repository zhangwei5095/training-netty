package com.shangpin.netty.chapter04.linebased;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * 粘包/拆包
 * 
 * @author percy
 *
 */
public class TimerServerHandler extends ChannelHandlerAdapter {
	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// 直接转换为字符串就可以，已经删除了回车换行
		String body = (String) msg;

		System.out.println("The time server receive order : " + body
				+ "; the counter is : " + ++counter);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(
				System.currentTimeMillis()).toString() : "BAD ORDER";
		currentTime = currentTime + System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// 我们发现还调用了ChannelHandlerContext的flush方法，
		// 它的作用是将消息发送队列中的消息写入到SocketChannel中发送给对方。
		// 从性能上考虑，为了防止频繁地唤醒Selector进行消息发送，Nettry的write方法并不直接将消息写入SocketChannel中
		// 调用write方法只是将把待发送的消息放到发送的缓冲数组中，再通过调用flush方法，将发送缓冲区的消息全部写到SocketChannel中。
		ctx.close();
	}

}

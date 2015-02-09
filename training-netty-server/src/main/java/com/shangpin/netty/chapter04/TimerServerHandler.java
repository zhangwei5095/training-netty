package com.shangpin.netty.chapter04;

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
		// 将msg转换为Netty的ByteBuf对象，ByteBuf对应JDK中的ByteBuffer对象，不过它提供了更强大和灵活的功能
		ByteBuf buf = (ByteBuf) msg;
		// 通过ByteBuf的readableBytes()方法可以获取缓冲区可读的字节数，根据可读的字节数创建byte数组。
		byte[] req = new byte[buf.readableBytes()];
		// 通过ByteBuf的readBytes方法将缓冲区的字节数组复制到新建的byte数组中
		buf.readBytes(req);
		// 最后通过new String构造函数获取请求信息。
		String body = new String(req, "UTF-8").substring(0, req.length
				- System.getProperty("line.separator").length());

		System.out.println("The time server receive order : " + body
				+ "; the counter is : " + ++counter);
		// 这时对请求消息判断，如果是"QUERY TIME ORDER"则创建应答消息，
		// 通过ChannelHandlerContext的write方法异步发送应答消息给
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(
				System.currentTimeMillis()).toString() : "BAD ORDER";
		currentTime = currentTime + System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		// 通过ChannelHandlerContext的write方法异步发送应答消息给
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

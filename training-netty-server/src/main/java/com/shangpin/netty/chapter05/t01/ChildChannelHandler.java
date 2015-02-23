package com.shangpin.netty.chapter05.t01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 主要增加LineBasedFrameDecoder
 * 
 * @author percy
 *
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 首先创建分隔符缓冲对象ByteBuf，本例中使用"$_"作为分隔符
		ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
		// 创建DelimiterBasedFrameDecoder对象，将其加入到ChannelPipeLine中。
		// 它有多个构造方法，这里我们传递两个参数，第一个参数表示单条消息的最大长度，当达到该长度后仍然美柚查到分隔符，就抛出TooLongFrameException异常。
		// 防止由于异常码缺失分隔符盗跖的内存溢出，这是Netty解码器的可靠性保护；
		// 第二个参数就是分隔符缓冲对象。
		ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new EchoServerHandler());
	}

}

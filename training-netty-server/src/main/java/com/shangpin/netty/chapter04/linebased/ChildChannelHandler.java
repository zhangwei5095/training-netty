package com.shangpin.netty.chapter04.linebased;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
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
		// 在原来的基础上新增了两个解码器：
		// 第一个就是LineBasedFrameDecoder
		// 第二个就是StringDecoder
		ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new TimerServerHandler());
	}

}

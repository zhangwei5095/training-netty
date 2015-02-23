package com.shangpin.netty.chapter05.t02;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
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
		ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
		ch.pipeline().addLast(new StringDecoder());
		ch.pipeline().addLast(new EchoServerHandler());
	}

}

package com.shangpin.netty.chapter10.t01;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 主要增加LineBasedFrameDecoder
 * 
 * @author percy
 *
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
	private final String url;

	public ChildChannelHandler(String url) {
		this.url = url;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 添加了HTTP请求消息解码器，随后又添加了HttpObjectAggregator解码器，它的作用是将多个消息转换为单一FullHttpRequest或者FullHttpResponse
		// 原因是HTTP解码器在每个HTTP消息中会生成多个消息对象。
		// HttpRequest/HttpResponse
		// HttpContent
		// LastHttpContent
		ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
		ch.pipeline().addLast("http-aggregator",
				new HttpObjectAggregator(65535));

		// 增加了HTTP响应解码器，对HTTP响应消息进行解码；
		ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
		// ChunkedWriteHandler 这里主要作用是支持异步发送大的码流，防止Java内存溢出错误。
		ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
		// 最后的添加HttpFileSeverHandler，用于处理文件服务器的业务逻辑处理。
		ch.pipeline().addLast("fileServerHandler",
				new HttpFileServerHandler(this.url));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}

package com.shangpin.netty.chapter07;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 主要增加LineBasedFrameDecoder
 * 
 * @author percy
 *
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		// 首先创建一个新的ObjectDecoder,它负责实现Serializable的POJO对象进行编码，
		// 它有多个构造函数，支持不同的ClassResolver,
		// 在此我们使用weakCachingConcurrentResolver创建安全的WeakReferenceMap
		// 对类加载器进行缓存，它支持多线程并发访问，当虚拟机内存不足时，会释放缓存中的内存，防止内存泄露。
		// 为了防止异常码流和解码错位导致的内存溢出，这里将单个对象最大序列化后的字节数组长度设置为1M，作为示例它已经足够使用。
		ch.pipeline().addLast(
				new ObjectDecoder(1024 * 1024, ClassResolvers
						.weakCachingConcurrentResolver(this.getClass()
								.getClassLoader())));

		// 这里增加了一个ObjectEncoder，它可以在消息发送的使用自动实现Serializable的POJO对象进行编码，因此用户无须亲自对对象手工序列化
		// 只需要关注自己 的业务逻辑处理即可，对象序列化和反序列化都由Netty的对象编码器搞定的。
		ch.pipeline().addLast(new ObjectEncoder());
		// 进行业务逻辑处理
		ch.pipeline().addLast(new SubRequestServerHandler());
	}

}

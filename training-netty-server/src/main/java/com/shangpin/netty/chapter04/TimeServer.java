package com.shangpin.netty.chapter04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 1.Channel <br>
 * 
 * <pre>
 * 	channel 是负责数据读，写的对象，有点类似于老的io里面的stream。
 * 	它和stream的区别，channel是双向的，既可以write 也可以read，而stream要分outstream和inputstream。
 * 	而且在NIO中用户不应该直接从channel中读写数据，而是应该通过buffer，通过buffer再将数据读写到channel中。
 * 	一个channel 可以提供给用户下面几个信息
 * 		(1)channel的当前状态，比如open 还是closed
 * 		(2)ChannelConfig对象，表示channel的一些参数，比如bufferSize
 * 		(3)channel支持的所有i/o操作（比如read,write,connect.bind）以及ChannelPipeLine（下面解释）
 * </pre>
 * 
 * <br>
 * 2.ChannelConfig <br>
 * 
 * <pre>
 * channel的参数，以Map 数据结构来存储
 * </pre>
 * 
 * <br>
 * 
 * 
 * 3.ChannelEvent<br>
 * 
 * <pre>
 * ChannelEvent广义的认为Channel相关的事件，它是否分Upstream events和downstream events两大块，
 * 这里需要注意的，若是以server为主体的话，从client的数据到server的过程是Upstream；
 * 而server到client的数据传输过程叫downstream；
 * 而如果以client为主体的话，从server到client的过程对client来说是Upstream，
 * 而client到server的过程对client来说就是downstream。
 * Upstream events包括：
 * messageReceived：信息被接受时 ---MessageEvent
 * exceptionCaught：产生异常时 ---ExceptionEvent
 * channelOpen：channel被开启时 ---ChannelStateEvent
 * channelClosed：channel被关闭时 ---ChannelStateEvent
 * channelBound：channel被开启并准备去连接但还未连接上的时候 ---ChannelStateEvent
 * channelUnbound：channel被开启不准备去连接时候 ---ChannelStateEvent
 * channelConnected：channel被连接上的时候 ---ChannelStateEvent
 * channelDisconnected:channel连接断开的时候 ---ChannelStateEvent
 * channelInterestChanged:Channel的interestOps被改变的时候 ------ChannelStateEvent
 * writeComplete:写到远程端完成的时候 --WriteCompletionEvent
 * 
 * Downstream events包括：
 * write：发送信息给channel的时候 --MessageEvent
 * bind：绑定一个channel到指定的本地地址 --ChannelStateEvent
 * unbind：解除当前本地端口的绑定--ChannelStateEvent
 * connect：将channel连接到远程的机 --ChannelStateEvent
 * disconnect：将channel与远程的机连接断开 --ChannelStateEvent
 * close：关闭channel --ChannelStateEvent
 * 
 * 需要注意的是，这里没有open event，
 * 这是因为当一个channel被channelFactory创建的话，channel总是已经被打开了。
 * 
 * 此外还有两个事件类型是当父channel存在子channel的情况
 * childChannelOpen：子channel被打开 ---ChannelStateEvent
 * childChannelClosed：子channel被关闭 ---ChannelStateEvent
 * 
 * </pre>
 * 
 * <br>
 * 
 * 4.ChannelHandler<br>
 * 
 * <pre>
 * channel是负责传送数据的载体，那么数据肯定需要根据要求进行加工处理，
 * 那么这个时候就用到ChannelHandler
 * 不同的加工可以构建不同的ChannelHandler，然后放入ChannelPipeline中
 * 此外需要有ChannelEvent触发后才能到达ChannelHandler，
 * 因此根据event不同有下面两种的sub接口ChannelUpstreamHandler和ChannelDownstreamHandler。
 * 一个ChannelHandler通常需要存储一些状态信息作为判断信息，常用做法定义一个变量
 * 比如
 * 
 * </pre>
 * 
 * <br>
 * 
 * 5.ChannelPipeline<br>
 * 
 * <pre>
 * channelPipeline是一系列channelHandler的集合，
 * 他参照J2ee中的Intercepting Filter模式来实现的，
 * 让用户完全掌握如果在一个handler中处理事件，
 * 同时让pipeline里面的多个handler可以相互交互。
 * 
 * Intercepting Filter：http://java.sun.com/blueprints/corej2eepatterns/Patterns/InterceptingFilter.html 
 * 对于每一个channel都需要有相应的channelPipeline,
 * 当为channel设置了channelPipeline后就不能再为channel重新设置 channelPipeline。
 * 此外建议的做法的通过Channels 这个帮助类来生成ChannelPipeline 而不是自己去构建ChannelPipeline
 * 
 * 通常pipeLine 添加多个handler，是基于业务逻辑的
 * 
 * 比如下面
 * 
 * {@link ChannelPipeline} p = {@link Channels}.pipeline();
 * p.addLast("1", new UpstreamHandlerA());
 * p.addLast("2", new UpstreamHandlerB());
 * p.addLast("3", new DownstreamHandlerA());
 * p.addLast("4", new DownstreamHandlerB());
 * p.addLast("5", new SimpleChannelHandler());
 * upstream event 执行的handler按顺序应该是 125
 * downstream event 执行的handler按顺序应该是 543
 * SimpleChannelHandler 是同时实现了 ChannelUpstreamHandler和ChannelDownstreamHandler的类
 * 上面只是具有逻辑，如果数据需要通过格式来进行编码的话，那需要这些写
 * {@link ChannelPipeline} pipeline = {@link Channels#pipeline() Channels.pipeline()};
 * pipeline.addLast("decoder", new MyProtocolDecoder());
 * pipeline.addLast("encoder", new MyProtocolEncoder());
 * pipeline.addLast("executor", new {@link ExecutionHandler}(new {@link OrderedMemoryAwareThreadPoolExecutor}(16, 1048576, 1048576)));
 * pipeline.addLast("handler", new MyBusinessLogicHandler());
 * 其中：
 * Protocol Decoder - 将binary转换为java对象
 * Protocol Encoder - 将java对象转换为binary
 * ExecutionHandler - applies a thread model.
 * Business Logic Handler - performs the actual business logic(e.g. database access)
 * 
 * 虽然不能为channel重新设置channelPipeline，但是channelPipeline本身是thread-safe，
 * 因此你可以在任何时候为channelPipeline添加删除channelHandler
 * 
 * 需要注意的是,下面的代码写法不能达到预期的效果
 * public class FirstHandler extends {@link SimpleChannelUpstreamHandler} {
 * 
 * {@code @Override}
 * public void messageReceived({@link ChannelHandlerContext} ctx, {@link MessageEvent} e) {
 * // Remove this handler from the pipeline,
 * ctx.getPipeline().remove(this);
 * // And let SecondHandler handle the current event.
 * ctx.getPipeline().addLast("2nd", new SecondHandler());
 * ctx.sendUpstream(e);
 * }
 * }
 * 
 * 前提现在Pipeline只有最后一个FirstHandler，
 * 上面明显是想把FirstHandler从Pipeline中移除，
 * 然后添加SecondHandler。而pipeline需要有一个Handler,因此如果想到到达这个效果，那么可以
 * 先添加SecondHandler，然后在移除FirstHandler。
 * 
 * </pre>
 * 
 * <br>
 * 
 * 6.ChannelFactory<br>
 * 
 * <pre>
 * channel的工厂类，也就是用来生成channel的类，
 * ChannelFactory根据指定的通信和网络来生成相应的channel，比如
 * NioServerSocketChannelFactory生成的channel是基于NIO server socket的。
 * 当一个channel创建后，ChannelPipeline将作为参数附属给该channel。
 * 对于channelFactory的关闭，需要做两步操作
 * 第一，关闭所有该factory产生的channel包括子channel。通常调用ChannelGroup#close()。
 * 第二，释放channelFactory的资源，调用releaseExternalResources()
 * 
 * </pre>
 * 
 * <br>
 * 7.ChannelGroup<br>
 * 
 * <pre>
 * channel的组集合，他包含一个或多个open的channel，closed channel会自动从group中移除，
 * 一个channel可以在一个或者多个channelGroup
 * 如果想将一个消息广播给多个channel，可以利用group来实现
 * 比如：
 * {@link ChannelGroup} recipients = new {@link DefaultChannelGroup}()
 * recipients.add(channelA);
 * recipients.add(channelB);
 * recipients.write(ChannelBuffers.copiedBuffer("Service will shut down for maintenance in 5 minutes.",CharsetUtil.UTF_8));
 * 
 * 当ServerChannel和非ServerChannel同时都在channelGroup中的时候，任何io请求的操作都是先在ServerChannel中执行再在其他Channel中执行。
 * 这个规则对关闭一个server非常适用。
 * 
 * </pre>
 * 
 * <br>
 * 
 * 8.ChannelFuture<br>
 * 
 * <pre>
 * 在netty中，所有的io传输都是异步，所有那么在传送的时候需要数据+状态来确定是否全部传送成功，而这个载体就是ChannelFuture。
 * </pre>
 * 
 * <br>
 * 
 * 9.ChannelGroupFuture<br>
 * 
 * <pre>
 * 针对一次ChannelGroup异步操作的结果，他和ChannelFuture一样，包括数据和状态。
 * 不同的是他由channelGroup里面channel的所有channelFuture 组成。
 * </pre>
 * 
 * <br>
 * 10.ChannelGroupFutureListener<br>
 * 
 * <pre>
 * 针对ChannelGroupFuture的监听器，同样建议使用ChannelGroupFutureListener而不是await();
 * </pre>
 * 
 * <br>
 * 
 * 11.ChannelFutureListener<br>
 * 
 * <pre>
 * ChannelFuture监听器，监听channelFuture的结果。
 * </pre>
 * 
 * <br>
 * 
 * 12.ChannelFutureProgressListener<br>
 * 
 * <pre>
 * 监听ChannelFuture处理过程，比如一个大文件的传送。
 * 而ChannelFutureListener只监听ChannelFuture完成未完成
 * </pre>
 * 
 * 13.ChannelHandlerContext<br>
 * 
 * <pre>
 * 如何让handler和他的pipeLine以及pipeLine中的其他handler交换，
 * 那么就要用到ChannelHandlerContext，
 * ChannelHandler可以通过ChannelHandlerContext的sendXXXstream(ChannelEvent)将event传给最近的handler ，
 * 可以通过ChannelHandlerContext的getPipeline来得到Pipeline，
 * 并修改他，ChannelHandlerContext还可以存放一下状态信息attments。
 * 一个ChannelHandler实例可以有一个或者多个ChannelHandlerContext
 * </pre>
 * 
 * <br>
 * 
 * 14.ChannelPipelineFactory<br>
 * 
 * <pre>
 * 产生ChannelPipe的工厂类
 * </pre>
 * 
 * <br>
 * 
 * 15.ChannelState<br>
 * 
 * <pre>
 * 记载channel状态常量
 * </pre>
 * 
 * <br>
 * 
 * 
 * 
 * @author percy
 *
 */
public class TimeServer {

	public void bind(int port) throws InterruptedException {
		// 创建两个线程组，专门用于网络事件处理。实际上就是Reactor线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// Netty启动NIO服务器端启动类，目的降低服务端开发的复杂度
			ServerBootstrap bootStrap = new ServerBootstrap();
			// 将两个NIO线程组当作入参传递到ServerBootstrap当中
			bootStrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			ChannelFuture future = bootStrap.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			// 优雅退出， 释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}
		new TimeServer().bind(port);
	}
}

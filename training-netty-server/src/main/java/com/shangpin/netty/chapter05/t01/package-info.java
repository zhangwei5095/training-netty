/**
 * DelimiterBasedFrameDecoder应用开发<br>
 * 
 * <pre>
 * 通过对DelimiterBasedFrameDecoder的使用，我们可以自动完成以分隔符作为码流结束标识的消息的解码。
 * 该章节来演示。
 * 
 * 演示程序以经典的Echo服务为例。EchoServer接收到EchoClient的请求消息后，将其打印出来，
 * 然后将原始的消息返回给客户端，消息以“$_”作为分隔符。
 * 
 * 
 * </pre>
 * 
 * 
 * @author percy
 *
 */
package com.shangpin.netty.chapter05.t01;
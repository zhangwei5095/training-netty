/**
 * HTTP协议开发应用<br>
 * <pre>
 * 介绍HTTP协议应用:
 * 特点：
 * 支持Client/Server模式；
 * 简单--客户端向服务器请求服务时，只需指定服务器的URL，携带必要的请求参数或者消息体
 * 灵活--HTTP协议允许传输任意类型的数据对象，传输的内容类型由HTTP消息头中的Content-Type加以标记
 * 无状态--HTTP协议是无状态协议，无状态是指协议对于事务处理没有记忆能力。缺少状态意味着如果后续处理需要之前的信息，则必须重传。
 * 	这样可能导致每次连接传送的数据量大。另一方面，在服务器不需要先前信息时它的应答就较快，负载较轻。
 * 
 * 
 * </pre>
 * URL<br>
 * <pre>
 * http://host[":"port]/[abs_path]
 * </pre>
 * 
 * HTTP请求消息[HttpRequest]<br>
 * 
 * HTTP请求由三部分组成,具体如下：<br>
 * <ul>
 * <li>HTTP请求行
 * <li>HTTP请求头
 * <li>HTTP请求正文
 * </ul>
 * <br>
 * <pre>
 * 请求行以一个方法符开头，以空格分开，后面跟着请求的URL和协议的版本，
 * 格式：Methon Request-URI HTTP-Version CRLF
 * 其中Method标识请求方法，Request-URI是一个桐姨资源标识符。
 * HTTP-Version标识协议的版本号
 * CRLF标识回车和换行
 * </pre>
 * <br>
 * 请求方法:<br>
 * <ul>
 * <li>GET
 * <li>POST
 * <li>HEAD
 * <li>PUT
 * <li>DELETE
 * <li>TRACE
 * <li>CONNECT
 * <li>OPTIONS
 * </ul>
 * <br>
 * <pre>
 * GET /netty5.0 HTTP/1.1
 * HOST: locahost:8080
 * Connection: keep-alive
 * User-Agent:Molilla/5.0 (Windows NT 5.0) AppleWebKit/537.1 Chome/21 Safari/537.1
 * Accept: text/html, application/xml; q=0.9, ;q=0.8
 * Accept-Encoding: gzip,deflate, sdch
 * Accept-Language: zh-CN, zh; q=0.8
 * Accept-Charset: GBK, utf-8;q=0.7, *;q=0.3
 * Content-Length: 0
 * </pre>
 * <br>
 * 
 * 
 * HTTP的部分请求消息头列表:<br>
 * <ul>
 * <li>Accept:
 * 	<pre>
 * 		请求报头域用于指定客户端接受哪些类型的信息。
 * 		例如：Accept: image/gif
 * 	</pre>
 * <li>Accept-Charset:	
 * 		<pre>
 * 			请求报头域用于指定客户端接受哪些字符集
 * 			例如:Accept-Charset:iso8859-1
 * 		</pre>
 * <li>Accept-Encoding: 
 * 		<pre>
 * 			请求报头域类似Accept,但是它用于指定可接受的内容编码
 * 			例如：Accept-Encoding:gzip.deflate
 * 		</pre>
 * <li>Accept-Language: 
 * 		<pre>
 * 			请求报头域，它用于之指定一种自然语言。
 * 			例如：Accept-Language: zh-cn
 * 		</pre>
 * <li>Authorization: 请求报头域主要用于证明客户端有权查看某个资源。
 * <li>Host: 发送请求时，该报头是必须的。用于指定请求资源的主机和端口号。
 * <li>User-Agent: 请求报头域允许客户端将它的操作系统、浏览器和其他属性告诉服务器。
 * <li>Content-Length:请求消息体的长度
 * <li>Content-Type: 标识后面的文档属于什么MIME类型。Servlet默认为text/plain.
 * <li>Connection: 连接类型
 * </ul>
 * 
 * <br>
 * HTTP响应消息<br>
 * 
 * 组成：<br>
 * <ul>
 * <li>状态行
 * <li>请求报头
 * <li>响应正文
 * </ul>
 * 
 * <br>
 * 状态行：<br>
 * <pre>
 * HTTP-Version Status-Code Reason-Phrase CRLF
 * </pre>
 * 
 * 
 * @author percy
 *
 */
package com.shangpin.netty.chapter10.t01;
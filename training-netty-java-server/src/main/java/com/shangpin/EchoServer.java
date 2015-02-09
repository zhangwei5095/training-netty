package com.shangpin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	public static void main(String[] args) throws IOException {

		ServerSocket server = null; // 定义ServerSocket类
		//Socket client = null;// 标示客户端
		BufferedReader buf = null;// 接收输入流
		PrintStream out = null;// 打印流输出最方便
		server = new ServerSocket(8888);// 服务器在8888端口上监听
		boolean f = true;//定义个标记位
		while(f) {
			System.out.println("服务器运行，等待客户端接入。。。");
			Socket client = server.accept();// 得到连接，程序进入到阻塞状态

			out = new PrintStream(client.getOutputStream()) ;//准备接收客户端的输入信息
			buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			boolean flag = true;//标志位，标示可以一直接收并回应消息
			while(flag) {
				String str = buf.readLine();//接收客户端发来的内容
				if(str == null || "".equals(str)) {// 标识没有内容
					flag = false;// 退出循环
				} else {
					if ("bye".equals(str)) {// 如果输入的内容是"bye"标示结束
						flag = false;
					}else {
						out.println("接收到客户端发来的消息内容：" + str);//回应客户端信息
					}
				}
			}
			client.close();// 客户端关闭
		}
		server.close();
	}

}

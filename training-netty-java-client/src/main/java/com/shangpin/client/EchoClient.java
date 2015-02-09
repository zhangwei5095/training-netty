package com.shangpin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Socket client = null;// 标示客户端
		client = new Socket("localhost", 8888);
		BufferedReader buf = null;// 一次性接收完成
		PrintStream out = null;// 发送数据
		BufferedReader input = null;// 接收键盘数据
		input = new BufferedReader(new InputStreamReader(System.in));
		buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintStream(client.getOutputStream());
		boolean flag = true;// 定义标记位
		while (flag) {
			System.out.println("输入信息：");
			out.print("\\r\\n");
			String str = input.readLine();
			out.println(str);
			
			if ("bye".equals(str)) {
				flag = false;
			} else {
				String echo = buf.readLine();// 接收返回结果
				System.out.println(echo);
			}
		}
		buf.close();
		client.close();
	}

}

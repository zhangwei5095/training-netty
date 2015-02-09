package com.shangpin;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloServer {
	
	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		Socket client = null;
		PrintStream out = null;
		server = new ServerSocket(8888);
		System.out.println("服务器运行，等待客户端接入。。。");
		client = server.accept();
		String str = "Hello World";
		out  = new PrintStream(client.getOutputStream());
		out.println(str);
		client.close();
		server.close();
	}
}

package com.shangpin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class HelloClient {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = null;
		client = new Socket("localhost", 8888);
		BufferedReader buf = null;
		buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
		String  str = buf.readLine();
		System.out.println("服务端输入的内容：" + str);
		buf.close();
		client.close();
		
	}

}

package com.shangpin.netty.chapter06.t01;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 测试使用默认的JDK序列化<br>
 * 经过测试默认的序列化不仅占用空间大，而且性能也不高。
 * 
 * @author percy
 *
 */
public class TestUserInfo {
	
	public static void main(String[] args) throws IOException {
		UserInfo info = new UserInfo();
		info.setId(100);
		info.setUsername("Hi Percy, Test it~");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(info);
		os.flush();
		os.close();
		
		byte[] b = bos.toByteArray();
		System.out.println("The jdk serializable length is : " + b.length);
		bos.close();
		System.out.println("========================================================");
		System.out.println("The byte array serializable length is  : " + info.codeC().length);
	}

}

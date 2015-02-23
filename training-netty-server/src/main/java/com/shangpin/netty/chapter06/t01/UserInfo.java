package com.shangpin.netty.chapter06.t01;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String username;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] codeC() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.username.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.id);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}

}

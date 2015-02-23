package com.shangpin.netty.chapter07;

import java.io.Serializable;

/**
 * 请求对象
 * 
 * @author percy
 *
 */
public class SubscribeRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private int requestId;
	private String username;
	private String productName;
	private String mobile;
	private String address;

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "SubscribeRequest [resquestId=" + this.requestId
				+ ",  username=" + username + ", productName=" + productName
				+ ",  mobile=" + mobile + "address=" + address + "]";
	}
}

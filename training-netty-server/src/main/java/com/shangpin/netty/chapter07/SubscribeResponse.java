package com.shangpin.netty.chapter07;

import java.io.Serializable;

public class SubscribeResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private int responseId;
	private int code;
	private String desc;

	public int getResponseId() {
		return responseId;
	}

	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "SubscribeResponse [responseId=" + this.responseId + " , code="
				+ this.code + " ,desc=" + desc + "]";
	}

}

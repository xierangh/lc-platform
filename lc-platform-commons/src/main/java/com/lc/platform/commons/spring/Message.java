package com.lc.platform.commons.spring;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 8117244181993856881L;
	
	private int status;
	private String statusText;
	private Object data;

	public Message() {
	}

	public Message(int status, String statusText) {
		this.statusText = statusText;
		this.status = status;
	}

	public Message(int status, String statusText, Object data) {
		this(status, statusText);
		this.data = data;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}

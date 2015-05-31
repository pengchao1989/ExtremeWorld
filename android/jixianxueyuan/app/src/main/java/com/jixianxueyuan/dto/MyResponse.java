package com.jixianxueyuan.dto;

public class MyResponse<T> {

    public static final int status_ok = 1;

	public int status;
	
	public T content;
	
	public static MyResponse ok(Object content)
	{
		MyResponse response = new MyResponse();
		response.setStatus(1);
		response.setContent(content);
		return response;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}
	
	
}

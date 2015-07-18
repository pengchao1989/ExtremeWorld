package com.yumfee.extremeworld.rest.dto;

public class MyResponse {

	public int status;
	
	public Object content;
	
	public static MyResponse ok(Object content)
	{
		MyResponse response = new MyResponse();
		response.setStatus(1);
		response.setContent(content);
		return response;
	}
	
	public static MyResponse create()
	{
		MyResponse response = new MyResponse();
		response.setStatus(201);
		return response;
	}
	
	public static MyResponse noContent()
	{
		MyResponse response = new MyResponse();
		response.setStatus(204);//错误码204
		response.setContent(null);
		return response;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	
}

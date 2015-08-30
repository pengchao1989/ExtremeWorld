package com.yumfee.extremeworld.rest.dto;

import com.yumfee.extremeworld.config.MyErrorCode;

public class MyResponse {
	
	public static int STATUS_OK = 1;
	public static int STATUS_ERROR = -1;

	public int status;
	
	public Object content;
	public ErrorInfo errorInfo;
	
	public static MyResponse ok(Object content)
	{
		MyResponse response = new MyResponse();
		response.setStatus(STATUS_OK);
		response.setContent(content);
		return response;
	}
	
	public static MyResponse err(MyErrorCode errorCode){
		MyResponse response = new MyResponse();
		response.setStatus(STATUS_ERROR);
		ErrorInfo error = new ErrorInfo();
		error.setErrorCode(errorCode.getErrorCode());
		error.setErrorInfo(errorCode.getErrorInfo());
		response.setErrorInfo(error);
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

	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	
	
	
}

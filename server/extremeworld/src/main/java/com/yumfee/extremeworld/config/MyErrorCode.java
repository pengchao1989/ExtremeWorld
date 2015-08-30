package com.yumfee.extremeworld.config;

public enum MyErrorCode {
	
	NAME_REPEAT(10001, "nick name repeat"), //昵称重复
	NAME_EMPTY(10002, "nick name empty");
	
	private int errorCode;
	private String errorInfo;
	

	private MyErrorCode(int errorCode, String errorInfo){
		this.errorCode = errorCode;
		this.errorInfo = errorInfo;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	
	
}

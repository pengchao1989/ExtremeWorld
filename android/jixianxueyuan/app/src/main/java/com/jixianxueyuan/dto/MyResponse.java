package com.jixianxueyuan.dto;

public class MyResponse<T> {

    public static final int status_ok = 1;
    public static final int status_error = -1;

	public int status;
	
	public T content;
    public ErrorInfo errorInfo;

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

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }
}

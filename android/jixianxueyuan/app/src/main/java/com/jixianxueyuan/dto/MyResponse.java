package com.jixianxueyuan.dto;

public class MyResponse<T> {

    public static final int status_ok = 1;
    public static final int status_error = -1;

	public int status;
	public boolean encryp;
	
	public T content;
    public Error error;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isEncryp() {
		return encryp;
	}

	public void setEncryp(boolean encryp) {
		this.encryp = encryp;
	}

    public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean isOK(){
		if (status == status_ok){
			return true;
		}
		return false;
	}
}

package com.yumfee.extremeworld.rest.dto.request;

/**
 * Created by pengchao on 8/21/15.
 */
public class HandshakeRequestDTO {
	private Long userId;
    private String hobbyStamp;
    private String device;

    
    
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getHobbyStamp() {
        return hobbyStamp;
    }

    public void setHobbyStamp(String hobbyStamp) {
        this.hobbyStamp = hobbyStamp;
    }

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
    
}

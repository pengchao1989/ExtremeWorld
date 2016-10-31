package com.yumfee.extremeworld.rest.dto.request;

/**
 * Created by pengchao on 8/21/15.
 */
public class HandshakeRequestDTO {
	private Long userId;
    private String hobbyStamp;
    private String device;
    private String plateForm;
    private String systemVersion;
    private String versionCode;
    private String versionName;
    
    
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

	public String getPlateForm() {
		return plateForm;
	}

	public void setPlateForm(String plateForm) {
		this.plateForm = plateForm;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
}

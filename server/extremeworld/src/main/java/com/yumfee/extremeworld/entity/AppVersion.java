package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_app_version")
public class AppVersion extends IdEntity{
	
	private String apkUrl;
	private String apkBarcodeUrl;
	private String apkVersion;
	private String updateLog;
	
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getApkBarcodeUrl() {
		return apkBarcodeUrl;
	}
	public void setApkBarcodeUrl(String apkBarcodeUrl) {
		this.apkBarcodeUrl = apkBarcodeUrl;
	}
	public String getApkVersion() {
		return apkVersion;
	}
	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
}

package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 10/5/15.
 */
public class AppVersion  implements Serializable{
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

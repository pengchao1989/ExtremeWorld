package com.jixianxueyuan.config;

import android.content.Context;

import com.jixianxueyuan.R;

/**
 * Created by pengchao on 8/30/15.
 */
public enum MyErrorCode {

    NAME_REPEAT(10001, "nick name repeat"), //昵称重复
    NAME_EMPTY(10002, "nick name empty"),
    PHONE_EMPTY(10003, "phone empty"),
    SMS_VERIFICATION_CODE_ERROR(10004,"send sms code error"),
    UNKNOW_ERROR(1001,"unknow error");

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

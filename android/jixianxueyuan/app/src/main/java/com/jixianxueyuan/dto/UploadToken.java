package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 6/24/15.
 */
public class UploadToken implements Serializable {
    private String uptoken;
    private String myParam;

    public String getUptoken() {
        return uptoken;
    }

    public void setUptoken(String uptoken) {
        this.uptoken = uptoken;
    }

    public String getMyParam() {
        return myParam;
    }

    public void setMyParam(String myParam) {
        this.myParam = myParam;
    }
}

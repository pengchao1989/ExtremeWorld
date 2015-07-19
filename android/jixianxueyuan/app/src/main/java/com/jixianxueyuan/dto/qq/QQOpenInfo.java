package com.jixianxueyuan.dto.qq;

import java.io.Serializable;

/**
 * Created by pengchao on 7/18/15.
 */
public class QQOpenInfo implements Serializable {
    String openid;
    String access_token;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}

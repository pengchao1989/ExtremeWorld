package com.jixianxueyuan.dto.request;

import com.jixianxueyuan.dto.UserInfoDTO;

/**
 * Created by pengchao on 7/19/15.
 */
public class UserInfoRequest extends UserInfoDTO {
    private String qqOpenId;

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }
}

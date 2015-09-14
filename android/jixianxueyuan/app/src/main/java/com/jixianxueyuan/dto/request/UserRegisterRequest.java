package com.jixianxueyuan.dto.request;

import com.jixianxueyuan.dto.UserDTO;

/**
 * Created by pengchao on 7/19/15.
 */
public class UserRegisterRequest extends UserDTO {
    private String qqOpenId;
    private String phone;
    private String password;

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

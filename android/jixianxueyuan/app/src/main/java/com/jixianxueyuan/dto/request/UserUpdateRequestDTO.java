package com.jixianxueyuan.dto.request;

import java.io.Serializable;

/**
 * Created by pengchao on 9/3/15.
 */
public class UserUpdateRequestDTO implements Serializable{
    private Long id;
    private String name;
    private String avatar;
    private String gender;
    private String signature;
    private String birth;
    private String bg;
    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getAvatar()
    {
        return avatar;
    }
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }
    public String getGender()
    {
        return gender;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }
}

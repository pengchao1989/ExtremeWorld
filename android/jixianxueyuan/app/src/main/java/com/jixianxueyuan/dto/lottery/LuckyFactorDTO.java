package com.jixianxueyuan.dto.lottery;


import com.jixianxueyuan.dto.UserMinDTO;

import java.util.Date;

/**
 * Created by pengchao on 17-4-22.
 */
public class LuckyFactorDTO {
    private Long id;
    private UserMinDTO user;
    private String type;
    private int number;
    private long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserMinDTO getUser() {
        return user;
    }

    public void setUser(UserMinDTO user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}

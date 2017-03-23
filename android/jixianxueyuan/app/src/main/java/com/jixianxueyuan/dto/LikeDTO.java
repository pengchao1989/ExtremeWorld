package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 17-3-17.
 */
public class LikeDTO implements Serializable {
    private Long id;
    private UserMinDTO user;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}

package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 8/9/15.
 */
public class RemindDTO implements Serializable {
    private String content;
    private String targetContent;
    private String targetType;
    private Long targetId;
    private String createTime;

    private UserMinDTO speaker;
    private UserMinDTO listener;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetContent() {
        return targetContent;
    }

    public void setTargetContent(String targetContent) {
        this.targetContent = targetContent;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserMinDTO getSpeaker() {
        return speaker;
    }

    public void setSpeaker(UserMinDTO speaker) {
        this.speaker = speaker;
    }

    public UserMinDTO getListener() {
        return listener;
    }

    public void setListener(UserMinDTO listener) {
        this.listener = listener;
    }
}

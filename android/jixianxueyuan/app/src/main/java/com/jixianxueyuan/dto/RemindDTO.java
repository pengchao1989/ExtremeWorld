package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 8/9/15.
 */
public class RemindDTO implements Serializable {

    private int type;
    private long sourceId;
    private String content;
    private String targetContent;
    private int targetType;
    private Long targetId;
    private String createTime;

    private UserMinDTO speaker;
    private UserMinDTO listener;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

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

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
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

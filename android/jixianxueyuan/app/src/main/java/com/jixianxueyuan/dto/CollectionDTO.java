package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 2/28/16.
 */
public class CollectionDTO implements Serializable {
    private long id;
    private String createTime;
    private TopicDTO topic;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public TopicDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicDTO topic) {
        this.topic = topic;
    }
}

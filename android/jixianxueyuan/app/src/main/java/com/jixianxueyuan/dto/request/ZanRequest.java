package com.jixianxueyuan.dto.request;

import java.io.Serializable;

/**
 * Created by pengchao on 6/28/15.
 */
public class ZanRequest implements Serializable {
    private Long topicId;
    private Long userId;

    public Long getTopicId() {
        return topicId;
    }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}

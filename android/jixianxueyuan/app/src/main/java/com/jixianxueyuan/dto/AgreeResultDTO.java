package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 6/28/15.
 */
public class AgreeResultDTO implements Serializable {

    private Long topicId;
    private int count;
    public Long getTopicId() {
        return topicId;
    }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

}

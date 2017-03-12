package com.jixianxueyuan.dto.request;

import java.io.Serializable;

/**
 * Created by pengchao on 2017/3/11.
 */
public class PushTopicDTO implements Serializable {

    private Long topicId;
    private int fine;

    public Long getTopicId() {
        return topicId;
    }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
    public int getFine() {
        return fine;
    }
    public void setFine(int fine) {
        this.fine = fine;
    }
}

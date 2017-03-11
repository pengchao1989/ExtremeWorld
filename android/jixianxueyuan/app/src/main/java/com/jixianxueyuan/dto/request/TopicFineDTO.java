package com.jixianxueyuan.dto.request;

/**
 * Created by sheodon on 2017/3/10.
 */
public class TopicFineDTO {

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

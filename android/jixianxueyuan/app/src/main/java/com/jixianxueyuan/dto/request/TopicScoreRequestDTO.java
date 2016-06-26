package com.jixianxueyuan.dto.request;

/**
 * Created by pengchao on 6/26/16.
 */
public class TopicScoreRequestDTO {
    private long topicId;
    private double score;

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

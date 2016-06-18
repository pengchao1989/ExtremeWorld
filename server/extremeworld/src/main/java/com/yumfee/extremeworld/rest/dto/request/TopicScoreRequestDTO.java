package com.yumfee.extremeworld.rest.dto.request;

public class TopicScoreRequestDTO {
	private Long topicId;
	private double score;
	
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
}

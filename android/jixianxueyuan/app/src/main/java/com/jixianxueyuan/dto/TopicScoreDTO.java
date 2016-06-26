package com.jixianxueyuan.dto;

public class TopicScoreDTO {
	private long id;
	private double score;
	private UserMinDTO user;
	
	private double topicAvgScore;
	private int topicTotalScoreCount;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public UserMinDTO getUser() {
		return user;
	}
	public void setUser(UserMinDTO user) {
		this.user = user;
	}
	public double getTopicAvgScore() {
		return topicAvgScore;
	}
	public void setTopicAvgScore(double topicAvgScore) {
		this.topicAvgScore = topicAvgScore;
	}
	public int getTopicTotalScoreCount() {
		return topicTotalScoreCount;
	}
	public void setTopicTotalScoreCount(int topicTotalScoreCount) {
		this.topicTotalScoreCount = topicTotalScoreCount;
	} 
}

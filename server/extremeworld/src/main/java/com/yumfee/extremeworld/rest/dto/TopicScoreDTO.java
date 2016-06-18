package com.yumfee.extremeworld.rest.dto;

public class TopicScoreDTO {
	private long id;
	private double score;
	private UserMinDTO user;
	
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
}

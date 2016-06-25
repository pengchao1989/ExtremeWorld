package com.yumfee.extremeworld.rest.dto;

public class UserScoreDTO {

	private double score;
	private UserMinDTO user;
	
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

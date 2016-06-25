package com.yumfee.extremeworld.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user_score")
public class UserScore extends IdEntity{

	private double score;
	private Date updateTime;
	
	private User user;
	private Hobby hobby;

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "hobby_id")
	public Hobby getHobby() {
		return hobby;
	}

	public void setHobby(Hobby hobby) {
		this.hobby = hobby;
	}
	
	
	
}

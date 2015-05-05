package com.yumfee.extremeworld.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//user的某项技能
@Entity
@Table(name = "tb_credit")
public class Credit extends IdEntity{

	private int status;
	
	private User user;
	private Course source;
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "credit_id")
	public Course getSource() {
		return source;
	}
	public void setSource(Course source) {
		this.source = source;
	}
	
	
}

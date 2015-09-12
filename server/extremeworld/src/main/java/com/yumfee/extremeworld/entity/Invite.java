package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_invite")
public class Invite{

	private String phone;
	private User inviteUser;
	
	@Id
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ManyToOne
	@JoinColumn(name = "invite_id")
	public User getInviteUser() {
		return inviteUser;
	}
	public void setInviteUser(User inviteUser) {
		this.inviteUser = inviteUser;
	}
	
	
}

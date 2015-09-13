package com.jixianxueyuan.dto;

public class InviteDTO {
	
	private Long id;
	private String phone;
	private UserMinDTO inviteUser;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public UserMinDTO getInviteUser() {
		return inviteUser;
	}
	public void setInviteUser(UserMinDTO inviteUser) {
		this.inviteUser = inviteUser;
	}
	
	
	
	
}

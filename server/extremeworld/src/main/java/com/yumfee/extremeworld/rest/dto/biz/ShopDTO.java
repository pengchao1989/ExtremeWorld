package com.yumfee.extremeworld.rest.dto.biz;

import java.util.Date;

import com.yumfee.extremeworld.rest.dto.UserMinDTO;

public class ShopDTO {
	private long id;
	private String name;
	private String description;
	private String address;
	private String cover;
	private Date createTime;
	private int weight;
	
	private UserMinDTO user;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public UserMinDTO getUser() {
		return user;
	}
	public void setUser(UserMinDTO user) {
		this.user = user;
	}
}

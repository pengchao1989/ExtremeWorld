package com.jixianxueyuan.dto.biz;

import com.jixianxueyuan.dto.UserMinDTO;

import java.io.Serializable;

public class ShopDTO implements Serializable{
	private long id;
	private String name;
	private String description;
	private String signature;
	private String address;
	private String cover;
	private String createTime;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
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

package com.yumfee.extremeworld.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "tb_group")
public class Group extends IdEntity{
	private String name;
	private String des;
	private String frontImg;
	private String address;
	private double longitude;
	private double latitude;
	private String getHash;
	private Date createTime;
	private int status;
	
	
	private User user;
	private Hobby hobby;
	
	private List<User> users;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getFrontImg() {
		return frontImg;
	}

	public void setFrontImg(String frontImg) {
		this.frontImg = frontImg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getGetHash() {
		return getHash;
	}

	public void setGetHash(String getHash) {
		this.getHash = getHash;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
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

	@ManyToOne
	@JoinColumn(name = "hobby_id")
	public Hobby getHobby() {
		return hobby;
	}

	public void setHobby(Hobby hobby) {
		this.hobby = hobby;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinTable(name = "tb_group_user",
	joinColumns = { @JoinColumn(name = "group_id", referencedColumnName = "id" ) },
	inverseJoinColumns = { @JoinColumn(name="user_id", referencedColumnName = "userId") })
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
	
}

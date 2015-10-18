package com.yumfee.extremeworld.entity.biz;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yumfee.extremeworld.entity.Hobby;
import com.yumfee.extremeworld.entity.IdEntity;
import com.yumfee.extremeworld.entity.User;

@Entity
@Table(name = "biz_shop")
public class Shop extends IdEntity{
	
	private String name;
	private String description;
	private String signature;
	private String address;
	private String cover;
	private Date createTime;
	private int weight;
	
	
	private User user;
	
	private List<Hobby> hobbys;
	
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
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "biz_shop_hobby",
	joinColumns = { @JoinColumn(name = "shop_id", referencedColumnName = "id" ) },
	inverseJoinColumns = { @JoinColumn(name="hobby_id", referencedColumnName = "id") })
	public List<Hobby> getHobbys() {
		return hobbys;
	}
	public void setHobbys(List<Hobby> hobbys) {
		this.hobbys = hobbys;
	}
	
	
	
	
}

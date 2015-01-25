package com.yumfee.extremeworld.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "tb_user_info")
@PrimaryKeyJoinColumn(name = "userId")
public class UserInfo extends User
{
	private String gender;
	private String birth;
	private String avatar;
	private String description;
	private String qq;
	private String weixin;
	
	private List<Hobby> hobbys = new ArrayList<Hobby>();
	private List<Site> sites = new ArrayList<Site>();
	
	public String getGender()
	{
		return gender;
	}
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	public String getBirth()
	{
		return birth;
	}
	public void setBirth(String birth)
	{
		this.birth = birth;
	}
	public String getAvatar()
	{
		return avatar;
	}
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getQq()
	{
		return qq;
	}
	public void setQq(String qq)
	{
		this.qq = qq;
	}
	public String getWeixin()
	{
		return weixin;
	}
	public void setWeixin(String weixin)
	{
		this.weixin = weixin;
	}
	
	//关系维护端,mappedBy属性定义了userinfo为双向关系的维护端
	//@ManyToMany(mappedBy = "userInfos")
	//关系被维护端
	//referencedColumnName指向对象的列名
	@ManyToMany()
	@JoinTable(name = "tb_user_hobby",
	joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "userId" ) },
	inverseJoinColumns = { @JoinColumn(name="hobby_id", referencedColumnName = "id") })
	public List<Hobby> getHobbys()
	{
		return hobbys;
	}
	public void setHobbys(List<Hobby> interests)
	{
		this.hobbys = interests;
	}
	
	@ManyToMany()
	@JoinTable(name = "tb_user_site",
	joinColumns = { @JoinColumn(name="user_id", referencedColumnName= "userId")},
	inverseJoinColumns = { @JoinColumn(name="site_id", referencedColumnName = "id") })
	public List<Site> getSites()
	{
		return sites;
	}
	public void setSites(List<Site> sites)
	{
		this.sites = sites;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

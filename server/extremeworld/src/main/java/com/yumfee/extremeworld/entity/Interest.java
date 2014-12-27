package com.yumfee.extremeworld.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_interest")
public class Interest extends IdEntity
{
	private String name;
	private String description;
	private Date createTime;
	
	private List<UserInfo> userInfos= new ArrayList<UserInfo>();
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	//关系被维护端
	//referencedColumnName指向对象的列名
	@ManyToMany(mappedBy = "interests")
	public List<UserInfo> getUserInfos()
	{
		return userInfos;
	}
	public void setUserInfos(List<UserInfo> userInfos)
	{
		this.userInfos = userInfos;
	}

	
	
	
}

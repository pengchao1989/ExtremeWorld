package com.yumfee.extremeworld.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_site")
public class Site extends IdEntity
{
	private String name;
	private String description;
	private int type;
	private String frontImg;
	private String address;
	private String longitude;
	private String latitude;
	private Date createTime;
	
	private CityGroup cityGroup;
	private List<User> users;
	
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
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public String getFrontImg()
	{
		return frontImg;
	}
	public void setFrontImg(String frontImg)
	{
		this.frontImg = frontImg;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getLongitude()
	{
		return longitude;
	}
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}
	public String getLatitude()
	{
		return latitude;
	}
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="city_group_id")
	public CityGroup getCityGroup()
	{
		return cityGroup;
	}
	public void setCityGroup(CityGroup cityGroup)
	{
		this.cityGroup = cityGroup;
	}
	
	//关系被维护端
	@ManyToMany(mappedBy = "sites")
	public List<User> getUsers()
	{
		return users;
	}
	public void setUsers(List<User> users)
	{
		this.users = users;
	}
	
	
	
	
	
}

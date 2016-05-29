package com.yumfee.extremeworld.rest.dto;

import java.util.Date;
import java.util.List;

public class SiteDTO
{
	private long id;
	private String name;
	private String description;
	private int type;
	private String frontImg;
	private String address;
	private String longitude;
	private String latitude;
	private Date createTime;
	
	private List<HobbyMinDTO> hobbys;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public List<HobbyMinDTO> getHobbys() {
		return hobbys;
	}
	public void setHobbys(List<HobbyMinDTO> hobbys) {
		this.hobbys = hobbys;
	}
	
}

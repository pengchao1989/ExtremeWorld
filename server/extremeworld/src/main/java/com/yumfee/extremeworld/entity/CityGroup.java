package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_city_group")
public class CityGroup extends IdEntity
{
	private String description;
	private String frontImg;
	private City city;
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getFrontImg()
	{
		return frontImg;
	}
	public void setFrontImg(String frontImg)
	{
		this.frontImg = frontImg;
	}
	
	@OneToOne
	@JoinColumn( name = "city_id" )
	public City getCity()
	{
		return city;
	}
	
	public void setCity(City city)
	{
		this.city = city;
	}
	
}

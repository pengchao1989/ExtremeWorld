package com.yumfee.extremeworld.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_city_group")
public class CityGroup extends IdEntity
{
	private String name;
	private String description;
	private String frontImg;
	private City city;
	
	
	
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

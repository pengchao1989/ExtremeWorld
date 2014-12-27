package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table( name = "s_city")
public class City extends IdEntity
{
	private String cityName;
	
	private CityGroup cityGroup;

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	@OneToOne(fetch=FetchType.LAZY, mappedBy="city")
	public CityGroup getCityGroup()
	{
		return cityGroup;
	}

	public void setCityGroup(CityGroup cityGroup)
	{
		this.cityGroup = cityGroup;
	}
	
	
	
}

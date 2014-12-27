package com.yumfee.extrmeworld.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CityGroup")
public class CityGroupDTO
{
	private String name;
	private String description;
	private String frontImg;
	
	private CityDTO city;

	
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

	public CityDTO getCity()
	{
		return city;
	}

	public void setCity(CityDTO city)
	{
		this.city = city;
	}
}

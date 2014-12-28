package com.yumfee.extrmeworld.rest.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "City")
public class ProvinceDTO
{
	private String provinceName;
	private List<CityDTO> citys;
	public String getProvinceName()
	{
		return provinceName;
	}
	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}
	public List<CityDTO> getCitys()
	{
		return citys;
	}
	public void setCitys(List<CityDTO> citys)
	{
		this.citys = citys;
	}
	
}

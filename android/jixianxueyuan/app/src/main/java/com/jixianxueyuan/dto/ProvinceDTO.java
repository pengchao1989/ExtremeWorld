package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.List;


public class ProvinceDTO implements Serializable
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

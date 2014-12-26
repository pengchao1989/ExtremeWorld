package com.yumfee.extremeworld.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "s_province")
public class Province extends IdEntity
{

	private String provinceName;

	private List<City> citys;
	

	public String getProvinceName()
	{
		return provinceName;
	}

	public void setProvinceName(String provinecName)
	{
		this.provinceName = provinecName;
	}

	@OneToMany
	@JoinColumn(name = "province_id")
	public List<City> getCitys()
	{
		return citys;
	}

	public void setCitys(List<City> citys)
	{
		this.citys = citys;
	}
	
	
	
	
}

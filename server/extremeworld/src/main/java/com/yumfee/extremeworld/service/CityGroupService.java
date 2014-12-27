package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yumfee.extremeworld.entity.CityGroup;
import com.yumfee.extremeworld.repository.CityGroupDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
public class CityGroupService
{
	private CityGroupDao cityGroupDao;

	public List<CityGroup> getAll()
	{
		return (List<CityGroup>) cityGroupDao.findAll();
	}
	
	@Autowired
	public void setCityGroupDao(CityGroupDao cityGroupDao)
	{
		this.cityGroupDao = cityGroupDao;
	}
	
	
	
}

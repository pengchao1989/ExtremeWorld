package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Activity;
import com.yumfee.extremeworld.repository.ActivityDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ActivityService
{
	private ActivityDao activityDao;

	public List<Activity> getAll()
	{
		return (List<Activity>) activityDao.findAll();
	}
	
	@Autowired
	public void setActivityDao(ActivityDao activityDao)
	{
		this.activityDao = activityDao;
	}
	
	
}

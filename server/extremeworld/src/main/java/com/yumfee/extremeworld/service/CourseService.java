package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.repository.CourseDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class CourseService
{
	private CourseDao courseDao;
	
	public Course getCourse(Long id)
	{
		return courseDao.findOne(id);
	}
	
	public void saveCourse(Course entity)
	{
		courseDao.save(entity);
	}

	@Autowired
	public void setCourseDao(CourseDao courseDao)
	{
		this.courseDao = courseDao;
	}
	
	
}

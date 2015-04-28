package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseBase;
import com.yumfee.extremeworld.entity.CourseVersion;
import com.yumfee.extremeworld.repository.CourseDao;
import com.yumfee.extremeworld.repository.CourseVersionDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class CourseService
{
	private CourseDao courseDao;
	private CourseVersionDao courseVersionDao;
	
	public CourseBase getCourse(Long id)
	{
		return courseDao.findOne(id);
	}
	
	public void saveCourse(Course entity)
	{
		courseDao.save(entity);
	}
	
	public void saveCourseVersion(CourseVersion entity)
	{
		courseVersionDao.save(entity);
	}
	
	public List<CourseBase> getRevisions(Long id)
	{
		return courseDao.findByPid(id);
	}

	@Autowired
	public void setCourseDao(CourseDao courseDao)
	{
		this.courseDao = courseDao;
	}

	@Autowired
	public void setCourseVersionDao(CourseVersionDao courseVersionDao) {
		this.courseVersionDao = courseVersionDao;
	}
	
	
	
	
}

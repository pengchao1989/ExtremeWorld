package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.CourseTaxonomy;
import com.yumfee.extremeworld.repository.CourseTaxonomyDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class CourseTaxonomyService
{
	private CourseTaxonomyDao courseTaxonomyDao;
	
	public CourseTaxonomy getCourseTaxonomy(Long id)
	{
		return courseTaxonomyDao.findOne(id);
	}
	
	public List<CourseTaxonomy> getAll()
	{
		return (List<CourseTaxonomy>) courseTaxonomyDao.findAll();
	}

	@Autowired
	public void setCourseTaxonomyDao(CourseTaxonomyDao courseTaxonomyDao)
	{
		this.courseTaxonomyDao = courseTaxonomyDao;
	}
	
	
	
}

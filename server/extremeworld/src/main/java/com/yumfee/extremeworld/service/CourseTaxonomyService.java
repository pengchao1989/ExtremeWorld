package com.yumfee.extremeworld.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseBase;
import com.yumfee.extremeworld.entity.CourseTaxonomy;
import com.yumfee.extremeworld.repository.CourseDao;
import com.yumfee.extremeworld.repository.CourseTaxonomyDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class CourseTaxonomyService
{
	private CourseTaxonomyDao courseTaxonomyDao;
	private CourseDao courseDao;
	
	public CourseTaxonomy getCourseTaxonomy(Long id)
	{
		return courseTaxonomyDao.findOne(id);
	}
	
	public List<CourseTaxonomy> getAll()
	{
/*		Specification<CourseTaxonomy> spec = new Specification<CourseTaxonomy>(){

			@Override
			public Predicate toPredicate(Root<CourseTaxonomy> root,CriteriaQuery<?> query, CriteriaBuilder cb) {

				ListJoin<CourseTaxonomy, Course> join = root.join(root.getModel().getList("courses",Course.class), JoinType.LEFT);
				
				Predicate p = cb.equal(join.get("t").as(String.class), "1");
				
				query.where(p);
				return query.getRestriction();
			}
			
		};*/
		
		
		List<CourseTaxonomy> courseTaxonomys = courseTaxonomyDao.find("1");
		return courseTaxonomys;
	}
	

	@Autowired
	public void setCourseTaxonomyDao(CourseTaxonomyDao courseTaxonomyDao)
	{
		this.courseTaxonomyDao = courseTaxonomyDao;
	}

	@Autowired
	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}
	
	
	
}

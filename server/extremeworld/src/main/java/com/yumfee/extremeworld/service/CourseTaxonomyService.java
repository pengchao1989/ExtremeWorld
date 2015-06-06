package com.yumfee.extremeworld.service;

import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseTaxonomy;
import com.yumfee.extremeworld.modules.nosql.redis.MyJedisExecutor;
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
		
/*		List<CourseTaxonomy> courseTaxonomys = MyJedisExecutor.lrangeList("courseTaxonomys", CourseTaxonomy.class);
		if(courseTaxonomys == null || courseTaxonomys.size() == 0)
		{
			courseTaxonomys = courseTaxonomyDao.findAll();
			MyJedisExecutor.rpushList("courseTaxonomys",courseTaxonomys);
		}
		
		//Load Course
		for(CourseTaxonomy courseTaxonomy : courseTaxonomys)
		{
			final Long taxonomyId = courseTaxonomy.getId();
			List<Course> courseList = new ArrayList<Course>();
			
			//获取某分类下的courseKey List
			List<String> courseKeyList = MyJedisExecutor.lrange("courseList:" + taxonomyId);
			//遍历coursetKey List 依次从redis取出course
			for(String courseKey : courseKeyList)
			{
				Course course = MyJedisExecutor.get(courseKey, Course.class);
				if(course != null)
				{
					courseList.add(course);
				}
			}
			
			//
			if(courseList.size() == 0)
			{
				System.out.println("taxonomyId=" + taxonomyId);
				courseList = courseDao.findByCourseTaxonomyIdAndType(taxonomyId, "course");

				for(Course course : courseList)
				{
					MyJedisExecutor.set("course:"+course.getId(), course);
					MyJedisExecutor.lpush("courseList:" + taxonomyId, "course:"+course.getId());
				}
			}
			
			courseTaxonomy.setCourses(courseList);
			
		}*/
		
		List<CourseTaxonomy> courseTaxonomys = courseTaxonomyDao.findAll();
		
		//再循环取出每个目录下的course
		for(CourseTaxonomy courseTaxonomy : courseTaxonomys)
		{
			List<Course> courses = courseDao.findByCourseTaxonomyIdAndType(courseTaxonomy.getId(), "course");
			courseTaxonomy.setCourses(courses);
		}
		

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

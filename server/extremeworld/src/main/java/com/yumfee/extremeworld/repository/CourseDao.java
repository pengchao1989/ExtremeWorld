package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Course;

public interface CourseDao extends PagingAndSortingRepository<Course,Long>
{

}

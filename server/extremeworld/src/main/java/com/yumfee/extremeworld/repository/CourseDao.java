package com.yumfee.extremeworld.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseBase;

public interface CourseDao extends PagingAndSortingRepository<CourseBase,Long>
{
	List<CourseBase> findByPid(Long id);
}

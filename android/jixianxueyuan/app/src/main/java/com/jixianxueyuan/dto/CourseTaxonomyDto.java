package com.jixianxueyuan.dto;

import java.util.Date;
import java.util.List;

public class CourseTaxonomyDTO
{
	private Long id;
	private String name;
	private String createTime;
	
	private List<CourseDTO> courses;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public List<CourseDTO> getCourses()
	{
		return courses;
	}

	public void setCourses(List<CourseDTO> courses)
	{
		this.courses = courses;
	}
	
	
}

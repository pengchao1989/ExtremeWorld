package com.jixianxueyuan.dto;

import java.util.Date;
import java.util.List;

public class CourseTaxonomyDto
{
	private Long id;
	private String name;
	private Date createTime;
	
	private List<CourseDto> courses;

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

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public List<CourseDto> getCourses()
	{
		return courses;
	}

	public void setCourses(List<CourseDto> courses)
	{
		this.courses = courses;
	}
	
	
}

package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.List;

public class CourseTaxonomyDTO implements Serializable
{
	private Long id;
	private String name;
	private String createTime;

	private List<CourseMinDTO> courses;

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

	public List<CourseMinDTO> getCourses() {
		return courses;
	}

	public void setCourses(List<CourseMinDTO> courses) {
		this.courses = courses;
	}
}

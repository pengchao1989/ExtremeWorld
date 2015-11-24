package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CourseTaxonomyDTO implements Serializable
{
	private Long id;
	private String name;
	private String createTime;

	private List<CourseCatalogueDTO> courseCatalogues;

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

	public List<CourseCatalogueDTO> getCourseCatalogues() {
		return courseCatalogues;
	}

	public void setCourseCatalogues(List<CourseCatalogueDTO> courseCatalogues) {
		this.courseCatalogues = courseCatalogues;
	}
}

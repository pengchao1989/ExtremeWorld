package com.jixianxueyuan.dto;

import java.util.Date;

public class CourseDTO
{
	private Long id;
	private String name;
	private String content;
	private String createTime;
	
	private UserInfoMinDTO userInfo;

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

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public UserInfoMinDTO getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserInfoMinDTO userInfo)
	{
		this.userInfo = userInfo;
	}
	
	
}

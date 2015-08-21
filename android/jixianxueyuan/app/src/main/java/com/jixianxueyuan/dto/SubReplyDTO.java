package com.jixianxueyuan.dto;

import java.io.Serializable;

public class SubReplyDTO implements Serializable
{
	private Long id;
	private String content;
	private String createTime;
	
	private UserMinDTO user;

	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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

	public UserMinDTO getUser()
	{
		return user;
	}

	public void setUser(UserMinDTO user)
	{
		this.user = user;
	}
	
	
}

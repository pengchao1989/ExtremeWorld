package com.yumfee.extrmeworld.rest.dto;

import java.util.Date;

public class SubReplyDTO
{
	private Long id;
	private String content;
	private Date createTime;
	
	private UserInfoMinDTO userInfo;

	
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

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
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

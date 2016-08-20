package com.yumfee.extremeworld.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SubReplyDTO
{
	private Long id;
	private String content;
	private Date createTime;
	
	private SubReplyDTO target;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public SubReplyDTO getTarget() {
		return target;
	}

	public void setTarget(SubReplyDTO target) {
		this.target = target;
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

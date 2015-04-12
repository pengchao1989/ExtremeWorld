package com.jixianxueyuan.dto;

import java.util.Date;

public class VideoDTO
{
	private Long id;
	private String title;
	private String content;
	private int imageCount;
	private int replyCount;
	private String createTime;
	private int status;
	
	private UserInfoMinDTO createUser;

	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getImageCount()
	{
		return imageCount;
	}

	public void setImageCount(int imageCount)
	{
		this.imageCount = imageCount;
	}

	public int getReplyCount()
	{
		return replyCount;
	}

	public void setReplyCount(int replyCount)
	{
		this.replyCount = replyCount;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public UserInfoMinDTO getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(UserInfoMinDTO createUser)
	{
		this.createUser = createUser;
	}
	
	
	
}

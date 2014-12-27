package com.yumfee.extrmeworld.rest.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yumfee.extremeworld.entity.UserInfo;

@XmlRootElement(name = "Topic")
public class TopicDTO
{
	private String title;
	private String content;
	private int imageCount;
	private int replyCount;
	private Date createTime;
	private int status;
	
	private UserInfoDTO createUser;
	
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
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
	public UserInfoDTO getCreateUser()
	{
		return createUser;
	}
	public void setCreateUser(UserInfoDTO createUser)
	{
		this.createUser = createUser;
	}

	
}

package com.yumfee.extremeworld.rest.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@XmlRootElement(name = "Topic")
public class TopicDTO
{
	private Long id;
	private String title;
	private String content;
	private int imageCount;
	private int replyCount;
	private Date createTime;
	private int status;
	private String videoSource;
	
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
	public String getVideoSource()
	{
		return videoSource;
	}
	public void setVideoSource(String videoSource)
	{
		this.videoSource = videoSource;
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

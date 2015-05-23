package com.yumfee.extremeworld.rest.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@XmlRootElement(name = "Topic")
public class TopicDTO
{
	private Long id;
	private String type;
	private String title;
	private String content;
	private int imageCount;
	private int replyCount;
	private int viewCount;
	private int agreeCount;
	private Date createTime;
	private int status;
	private String videoSource;
	
	private UserMinDTO user;
	
	private MediaWrapDTO  mediaWrap;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String typec) {
		this.type = typec;
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
	
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getAgreeCount() {
		return agreeCount;
	}
	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
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
	public UserMinDTO getUser()
	{
		return user;
	}
	public void setUser(UserMinDTO user)
	{
		this.user = user;
	}
	public MediaWrapDTO getMediaWrap() {
		return mediaWrap;
	}
	public void setMediaWrap(MediaWrapDTO mediaWrap) {
		this.mediaWrap = mediaWrap;
	}
	
	
}

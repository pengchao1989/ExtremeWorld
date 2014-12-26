package com.yumfee.extremeworld.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_activity_detail")
public class ActivityDetail extends IdEntity
{
	private String content;
	private String frontImg;
	private Date beginTime;
	private Date endTime;
	
	private Site site;
	
	
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getFrontImg()
	{
		return frontImg;
	}
	public void setFrontImg(String frontImg)
	{
		this.frontImg = frontImg;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getBeginTime()
	{
		return beginTime;
	}
	public void setBeginTime(Date beginTime)
	{
		this.beginTime = beginTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	@ManyToOne
	@JoinColumn(name = "site_id")
	public Site getSite()
	{
		return site;
	}
	public void setSite(Site site)
	{
		this.site = site;
	}
	
	
}

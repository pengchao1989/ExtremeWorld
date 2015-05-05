package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_video_detail")
public class VideoDetail  
{
	private Long id;
	private String videoSource;
	private String thumbnail;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId()
	{
		return id;
	}
	public void setId(Long videoId)
	{
		this.id = videoId;
	}
	
	
	public String getVideoSource()
	{
		return videoSource;
	}

	public void setVideoSource(String videoSource)
	{
		this.videoSource = videoSource;
	}
	public String getThumbnail()
	{
		return thumbnail;
	}
	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}
	
	
	
}

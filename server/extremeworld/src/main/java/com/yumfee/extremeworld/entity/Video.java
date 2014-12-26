package com.yumfee.extremeworld.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class Video extends Topic
{
	private String videoSource;

	public String getVideoSource()
	{
		return videoSource;
	}

	public void setVideoSource(String videoSource)
	{
		this.videoSource = videoSource;
	}
	
}

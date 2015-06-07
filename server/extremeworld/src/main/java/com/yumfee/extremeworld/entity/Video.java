package com.yumfee.extremeworld.entity;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

//type=4  typec="video"

@Entity
@DiscriminatorValue("4")
public class Video extends Topic
{
	private VideoDetail videoDetail;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	public VideoDetail getVideoDetail()
	{
		return videoDetail;
	}

	public void setVideoDetail(VideoDetail videoDetail)
	{
		this.videoDetail = videoDetail;
	}
	
	
}

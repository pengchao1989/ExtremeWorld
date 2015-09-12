package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.yumfee.extremeworld.proto.VideoDetailProto;
import com.yumfee.extremeworld.proto.VideoDetailProto.VideoDetailDTO;

@Entity
@Table(name = "tb_video_detail")
public class VideoDetail extends IdEntity
{
	private String videoSource;
	private String thumbnail;
	
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

	public VideoDetailDTO builVideoDetaildDTO() {
		return VideoDetailProto.VideoDetailDTO.newBuilder()
		.setId(this.id)
		.setVideoSource(this.videoSource)
		.setThumbnail(this.thumbnail)
		.build();
	}
	
}

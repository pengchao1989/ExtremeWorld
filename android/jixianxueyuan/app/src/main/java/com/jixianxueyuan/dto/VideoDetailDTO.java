package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.List;

public class VideoDetailDTO implements Serializable {

	private Long id;
	private String videoSource;
	private String thumbnail;

	private List<VideoExtraDTO> videoExtraList;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVideoSource() {
		return videoSource;
	}
	public void setVideoSource(String videoSource) {
		this.videoSource = videoSource;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<VideoExtraDTO> getVideoExtraList() {
		return videoExtraList;
	}

	public void setVideoExtraList(List<VideoExtraDTO> videoExtraList) {
		this.videoExtraList = videoExtraList;
	}
}

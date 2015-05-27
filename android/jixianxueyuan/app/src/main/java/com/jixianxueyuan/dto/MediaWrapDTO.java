package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class MediaWrapDTO implements Serializable {

	private String createTime;
	private List<MediaDTO> medias;

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<MediaDTO> getMedias() {
		return medias;
	}
	public void setMedias(List<MediaDTO> medias) {
		this.medias = medias;
	}
	
}

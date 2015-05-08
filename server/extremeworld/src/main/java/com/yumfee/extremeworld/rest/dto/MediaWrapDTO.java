package com.yumfee.extremeworld.rest.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@XmlRootElement(name = "MediaWrap")
public class MediaWrapDTO {

	private Date createTime;
	private List<MediaDTO> medias;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<MediaDTO> getMedias() {
		return medias;
	}
	public void setMedias(List<MediaDTO> medias) {
		this.medias = medias;
	}
	
}

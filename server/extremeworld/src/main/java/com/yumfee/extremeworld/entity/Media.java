package com.yumfee.extremeworld.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_media")
public class Media extends IdEntity{

	private String path;
	private String des;
	private String type;//img video music
	
	//private MediaWrap mediaWrap;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
/*	@ManyToOne(cascade = { CascadeType.ALL}, optional = true)
	@JoinColumn(name = "mediawrap_id")
	public MediaWrap getMediaWrap() {
		return mediaWrap;
	}
	public void setMediaWrap(MediaWrap mediaWrap) {
		this.mediaWrap = mediaWrap;
	}*/
	
	
	
}

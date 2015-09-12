package com.yumfee.extremeworld.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yumfee.extremeworld.proto.MediaWrapProto.MediaWrapDTO;

@Entity
@Table(name = "tb_mediawrap")
public class MediaWrap extends IdEntity{

	private Date createTime;
	
	private List<Media> medias;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity=Media.class)
	@JoinColumn(name="mediawrap_id", unique=true, nullable=false, updatable=false)
	public List<Media> getMedias() {
		return medias;
	}
	
	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}
	
	public MediaWrapDTO buildMediaWrapDTO(){
		MediaWrapDTO.Builder builder =  MediaWrapDTO.newBuilder()
				.setCreateTime(this.createTime.toString());
		
		for(Media media : medias){
			builder.addMedias(media.buildMediaDTO());
		}
		
		return builder.build();
	}
}

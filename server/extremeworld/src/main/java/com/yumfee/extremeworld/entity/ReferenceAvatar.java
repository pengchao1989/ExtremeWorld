package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_reference_avatar")
public class ReferenceAvatar extends IdEntity{

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}

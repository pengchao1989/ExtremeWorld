package com.yumfee.extremeworld.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_app_config")
public class AppConfig extends IdEntity{

	private Boolean openInvitation;

	public Boolean getOpenInvitation() {
		return openInvitation;
	}

	public void setOpenInvitation(Boolean openInvitation) {
		this.openInvitation = openInvitation;
	}
	
	
}

package com.yumfee.extremeworld.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "appConfig")
public class AppConfigDTO {
	private Boolean openInvitation;

	public Boolean getOpenInvitation() {
		return openInvitation;
	}

	public void setOpenInvitation(Boolean openInvitation) {
		this.openInvitation = openInvitation;
	}
}

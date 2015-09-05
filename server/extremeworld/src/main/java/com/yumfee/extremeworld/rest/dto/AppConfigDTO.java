package com.yumfee.extremeworld.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "appConfig")
public class AppConfigDTO {
	private Boolean openInvitation;
	private String invitationDesTitle;
	private String invitationDesUrl;

	public Boolean getOpenInvitation() {
		return openInvitation;
	}

	public void setOpenInvitation(Boolean openInvitation) {
		this.openInvitation = openInvitation;
	}

	public String getInvitationDesTitle() {
		return invitationDesTitle;
	}

	public void setInvitationDesTitle(String invitationDesTitle) {
		this.invitationDesTitle = invitationDesTitle;
	}

	public String getInvitationDesUrl() {
		return invitationDesUrl;
	}

	public void setInvitationDesUrl(String invitationDesUrl) {
		this.invitationDesUrl = invitationDesUrl;
	}
	
}

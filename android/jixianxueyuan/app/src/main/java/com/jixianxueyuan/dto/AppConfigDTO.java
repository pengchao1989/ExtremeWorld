package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 9/5/15.
 */
public class AppConfigDTO implements Serializable {

    private Boolean openInvitation;
    private String invitationDesTitle;
    private String invitationDesUrl;

    public Boolean getOpenInvitation() {
        return openInvitation;
    }

    public void setOpenInvitation(Boolean openInvitation) {
        this.openInvitation = openInvitation;
    }

    public String getInvitationDesUrl() {
        return invitationDesUrl;
    }

    public void setInvitationDesUrl(String invitationDesUrl) {
        this.invitationDesUrl = invitationDesUrl;
    }

    public String getInvitationDesTitle() {
        return invitationDesTitle;
    }

    public void setInvitationDesTitle(String invitationDesTitle) {
        this.invitationDesTitle = invitationDesTitle;
    }
}

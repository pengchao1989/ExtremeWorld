package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 9/5/15.
 */
public class AppConfigDTO implements Serializable {

    private Boolean openInvitation;

    public Boolean getOpenInvitation() {
        return openInvitation;
    }

    public void setOpenInvitation(Boolean openInvitation) {
        this.openInvitation = openInvitation;
    }
}

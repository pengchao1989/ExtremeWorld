package com.jixianxueyuan.config;

/**
 * Created by pengchao on 9/4/15.
 */
public enum UploadPrefixName {

    AVATAR("avatar_"),
    VIDEO("video_"),
    COVER("cover_"),
    SITE("site_");

    private String prefixName;

    private UploadPrefixName(String prefixName){
        this.prefixName = prefixName;
    }

    public String getPrefixName() {
        return prefixName;
    }
}

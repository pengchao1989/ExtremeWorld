package com.jixianxueyuan.config;

/**
 * Created by pengchao on 9/4/15.
 */
public enum UploadPrefixName {

    AVATAR("avatar"),
    VIDEO("video");

    private String prefixName;

    private UploadPrefixName(String prefixName){
        this.prefixName = prefixName;
    }

    public String getPrefixName() {
        return prefixName;
    }
}

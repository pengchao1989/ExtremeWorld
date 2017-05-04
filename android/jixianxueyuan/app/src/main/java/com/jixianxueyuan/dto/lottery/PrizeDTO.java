package com.jixianxueyuan.dto.lottery;


import com.jixianxueyuan.dto.MediaWrapDTO;

import java.util.Date;

/**
 * Created by pengchao on 17-4-22.
 */
public class PrizeDTO {
    private Long id;
    private String name;
    private String des;
    private long createTime;
    private MediaWrapDTO mediaWrap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public MediaWrapDTO getMediaWrap() {
        return mediaWrap;
    }

    public void setMediaWrap(MediaWrapDTO mediaWrap) {
        this.mediaWrap = mediaWrap;
    }
}

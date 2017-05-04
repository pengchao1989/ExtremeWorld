package com.jixianxueyuan.dto.lottery;

import java.io.Serializable;

/**
 * Created by pengchao on 17-4-22.
 */
public class LotteryPlanDTO implements Serializable{

    private Long id;
    private String title;
    private String des;
    private String period;
    private String type;
    private long createTime;
    private long lotteryTime;
    private long joinBeginTime;
    private long joinEndTime;
    private String calcFactor;
    private String status;
    private String sponsor;
    private String sponsorDes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(long lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public long getJoinBeginTime() {
        return joinBeginTime;
    }

    public void setJoinBeginTime(long joinBeginTime) {
        this.joinBeginTime = joinBeginTime;
    }

    public long getJoinEndTime() {
        return joinEndTime;
    }

    public void setJoinEndTime(long joinEndTime) {
        this.joinEndTime = joinEndTime;
    }

    public String getCalcFactor() {
        return calcFactor;
    }

    public void setCalcFactor(String calcFactor) {
        this.calcFactor = calcFactor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorDes() {
        return sponsorDes;
    }

    public void setSponsorDes(String sponsorDes) {
        this.sponsorDes = sponsorDes;
    }
}

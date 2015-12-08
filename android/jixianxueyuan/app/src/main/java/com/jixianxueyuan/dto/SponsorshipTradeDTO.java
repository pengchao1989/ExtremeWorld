package com.jixianxueyuan.dto;

/**
 * Created by pengchao on 12/7/15.
 */
public class SponsorshipTradeDTO {
    private Long id;
    private double sum;
    private String message;
    private String createTime;

    private UserMinDTO user;
    private TradeDTO trade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserMinDTO getUser() {
        return user;
    }

    public void setUser(UserMinDTO user) {
        this.user = user;
    }

    public TradeDTO getTrade() {
        return trade;
    }

    public void setTrade(TradeDTO trade) {
        this.trade = trade;
    }
}

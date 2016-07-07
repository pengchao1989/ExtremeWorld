package com.jixianxueyuan.dto;

/**
 * Created by pengchao on 7/7/16.
 */
public class UserScoreDTO {
    private double score;
    private UserMinDTO user;

    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public UserMinDTO getUser() {
        return user;
    }
    public void setUser(UserMinDTO user) {
        this.user = user;
    }
}

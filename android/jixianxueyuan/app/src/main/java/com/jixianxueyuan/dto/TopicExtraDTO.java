package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pengchao on 4/6/16.
 */
public class TopicExtraDTO implements Serializable {
    private boolean agreed;
    private boolean collected;
    private double myMarkScore;
    private List<LikeDTO> likeList;

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public boolean isCollected() {
        return collected;
    }

    public double getMyMarkScore() {
        return myMarkScore;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public void setMyMarkScore(double myMarkScore) {
        this.myMarkScore = myMarkScore;
    }

    public List<LikeDTO> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeDTO> likeList) {
        this.likeList = likeList;
    }
}

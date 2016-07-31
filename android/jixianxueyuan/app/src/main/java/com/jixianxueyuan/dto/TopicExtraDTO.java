package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 4/6/16.
 */
public class TopicExtraDTO implements Serializable {
    private boolean agreed;
    private boolean collected;
    private double myMarkScore;

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
}

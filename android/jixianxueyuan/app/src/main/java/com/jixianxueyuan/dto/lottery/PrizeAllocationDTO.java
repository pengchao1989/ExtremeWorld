package com.jixianxueyuan.dto.lottery;

/**
 * Created by pengchao on 17-4-22.
 */
public class PrizeAllocationDTO {

    private Long id;
    private PrizeDTO prize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrizeDTO getPrize() {
        return prize;
    }

    public void setPrize(PrizeDTO prize) {
        this.prize = prize;
    }
}

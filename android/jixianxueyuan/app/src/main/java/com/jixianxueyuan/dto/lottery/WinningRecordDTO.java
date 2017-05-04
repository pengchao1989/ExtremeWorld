package com.jixianxueyuan.dto.lottery;


import com.jixianxueyuan.dto.UserMinDTO;

import java.util.Date;

/**
 * Created by pengchao on 17-4-22.
 */
public class WinningRecordDTO {

    private Long id;
    private PrizeAllocationDTO prizeAllocation;
    private UserMinDTO user;
    private long createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrizeAllocationDTO getPrizeAllocation() {
        return prizeAllocation;
    }

    public void setPrizeAllocation(PrizeAllocationDTO prizeAllocation) {
        this.prizeAllocation = prizeAllocation;
    }

    public UserMinDTO getUser() {
        return user;
    }

    public void setUser(UserMinDTO user) {
        this.user = user;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}

package com.jixianxueyuan.dto.lottery;


import java.util.List;

/**
 * Created by pengchao on 17-4-23.
 */
public class LotteryPlanDetailOfUserDTO {
    //抽奖计划
    private LotteryPlanDTO lotteryPlan;
    //奖品列表
    private List<PrizeAllocationDTO> prizeAllocationList;
    //开奖结果
    private List<WinningRecordDTO> winningRecordList;
    //幸运因子总数
    private Long totalLuckyFactorCount;
    //我的幸运因子数
    private Long userLuckyFactorCount;
    //我的中奖概率
    private double userWinningProbability;
    //用户是否能够领取幸运因子
    private boolean isCanGetLuckyFactor;

    public LotteryPlanDTO getLotteryPlan() {
        return lotteryPlan;
    }

    public void setLotteryPlan(LotteryPlanDTO lotteryPlan) {
        this.lotteryPlan = lotteryPlan;
    }

    public List<PrizeAllocationDTO> getPrizeAllocationList() {
        return prizeAllocationList;
    }

    public void setPrizeAllocationList(List<PrizeAllocationDTO> prizeAllocationList) {
        this.prizeAllocationList = prizeAllocationList;
    }

    public List<WinningRecordDTO> getWinningRecordList() {
        return winningRecordList;
    }

    public void setWinningRecordList(List<WinningRecordDTO> winningRecordList) {
        this.winningRecordList = winningRecordList;
    }

    public Long getTotalLuckyFactorCount() {
        return totalLuckyFactorCount;
    }

    public void setTotalLuckyFactorCount(Long totalLuckyFactorCount) {
        this.totalLuckyFactorCount = totalLuckyFactorCount;
    }

    public Long getUserLuckyFactorCount() {
        return userLuckyFactorCount;
    }

    public void setUserLuckyFactorCount(Long userLuckyFactorCount) {
        this.userLuckyFactorCount = userLuckyFactorCount;
    }

    public double getUserWinningProbability() {
        return userWinningProbability;
    }

    public void setUserWinningProbability(double userWinningProbability) {
        this.userWinningProbability = userWinningProbability;
    }

    public boolean isCanGetLuckyFactor() {
        return isCanGetLuckyFactor;
    }

    public void setIsCanGetLuckyFactor(boolean canGetLuckyFactor) {
        isCanGetLuckyFactor = canGetLuckyFactor;
    }
}

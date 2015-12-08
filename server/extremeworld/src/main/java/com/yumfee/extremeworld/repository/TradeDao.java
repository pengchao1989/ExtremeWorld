package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Trade;


public interface TradeDao extends PagingAndSortingRepository<Trade, Long>{

	public Trade findByInternalTradeNo(String internalTradeNo);
}

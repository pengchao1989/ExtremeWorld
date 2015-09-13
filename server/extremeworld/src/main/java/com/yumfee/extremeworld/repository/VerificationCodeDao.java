package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.VerificationCode;

public interface VerificationCodeDao extends PagingAndSortingRepository<VerificationCode,Long>{

	public VerificationCode findByPhoneAndCode(String phone,String code);
}

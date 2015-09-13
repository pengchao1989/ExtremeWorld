package com.yumfee.extremeworld.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.VerificationCode;
import com.yumfee.extremeworld.repository.VerificationCodeDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class VerificationCodeService {

	@Autowired
	private VerificationCodeDao verificationCodeDao;
	
	public void saveVerificationCode(VerificationCode verificationCode){
		verificationCodeDao.save(verificationCode);
	}
	
	public VerificationCode getVerificationCodeByPhoneAndCode(String phone,String code){
		return verificationCodeDao.findByPhoneAndCode(phone, code);
	}
}

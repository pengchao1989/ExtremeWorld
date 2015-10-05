package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.AppVersion;
import com.yumfee.extremeworld.repository.AppVersionDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class AppVersionService {
	private AppVersionDao appVersionDao;

	public AppVersion getAppVersion(Long id){
		return appVersionDao.findOne(id);
	}

	@Autowired
	public void setAppVersionDao(AppVersionDao appVersionDao) {
		this.appVersionDao = appVersionDao;
	}
	
	
}

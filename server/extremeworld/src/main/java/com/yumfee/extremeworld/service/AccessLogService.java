package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.AccessLog;
import com.yumfee.extremeworld.repository.AccessLogDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class AccessLogService {
	private AccessLogDao accessLogDao;

	public void add(String url , String log){
		AccessLog accessLog = new AccessLog();
		accessLog.setUrl(url);
		accessLog.setLog(log);
		accessLogDao.save(accessLog);
	}

	@Autowired
	public void setAccessLogDao(AccessLogDao accessLogDao) {
		this.accessLogDao = accessLogDao;
	}
	
	

}

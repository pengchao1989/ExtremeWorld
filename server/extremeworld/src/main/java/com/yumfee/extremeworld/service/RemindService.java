package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Remind;
import com.yumfee.extremeworld.repository.RemindDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class RemindService {

	@Autowired
	private RemindDao remindDao;
	
	public Page<Remind> getAll(Long userId, int pageNumber, int pageSize){
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize);
		return remindDao.findByListenerId(userId, pageRequest);
	}
}

package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Invite;
import com.yumfee.extremeworld.repository.InviteDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class InviteService {

	@Autowired
	private InviteDao inviteDao;
	
	public void saveInvite(Invite invite){
		inviteDao.save(invite);
	}
	
	public Invite getInvite(Long id){
		return inviteDao.findOne(id);
	}
}

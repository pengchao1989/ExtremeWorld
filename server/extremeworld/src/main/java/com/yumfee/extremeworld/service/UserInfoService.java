package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.UserInfo;
import com.yumfee.extremeworld.repository.UserInfoDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class UserInfoService {

	@Autowired
	private UserInfoDao userInfoDao;
	
	public List<UserInfo> getFollowings(Long id)
	{
		return userInfoDao.findOne(id).getFollowings();
	}
	
	public List<UserInfo> getFollowers(Long id)
	{
		return userInfoDao.findOne(id).getFollowers();
	}
}

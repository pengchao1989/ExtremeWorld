package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.UserInfo;


public interface UserInfoDao extends PagingAndSortingRepository<UserInfo, Long>
{
	UserInfo findByLoginName(String loginName);
	UserInfo findByQqOpenId(String qqOpenId);
}

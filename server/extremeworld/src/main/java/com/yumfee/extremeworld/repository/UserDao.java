package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.User;


public interface UserDao extends PagingAndSortingRepository<User, Long>
{
	User findById(Long id);
	User findByLoginName(String loginName);
	User findByName(String name);
	User findByPhone(String phone);
	User findByQqOpenId(String qqOpenId);
	
	Page<User> findByGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLike(
			String center,
			String north,
			String east,
			String south,
			String west,
			String northwest,
			String northeast,
			String southwest,
			String southeast,Pageable pageable);
	Page<User> findByInviterIdAndInited(Long inviterId, boolean inited, Pageable pageable);
	Page<User> findByInviterId(Long inviterId, Pageable pageable);
}

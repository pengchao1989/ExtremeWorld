/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.yumfee.extremeworld.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
	User findByLoginName(String loginName);
	User findByQqOpenId(String qqOpenId);
}

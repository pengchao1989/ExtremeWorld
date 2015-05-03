/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.yumfee.extremeworld.entity.UserBase;

public interface UserDao extends PagingAndSortingRepository<UserBase, Long> {
	UserBase findByLoginName(String loginName);
	UserBase findByQqOpenId(String qqOpenId);
}

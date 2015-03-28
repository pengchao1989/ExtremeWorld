/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.yumfee.extremeworld.data;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.UserInfo;

import org.springside.modules.test.data.RandomData;

public class UserData {

	public static UserInfo randomNewUser() {
		UserInfo user = new UserInfo();
		user.setLoginName(RandomData.randomName("user"));
		user.setName(RandomData.randomName("User"));
		user.setPlainPassword(RandomData.randomName("password"));

		return user;
	}
}

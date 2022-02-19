package com.sml.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 프로그램명  : UserServiceImpl
 * 날짜       : 2022-02-13 / 일요일 / 오후 6:57
 * 설명       :
 */

@Service
public class UserService {

	@Autowired
	private UserMapper mapper;
}

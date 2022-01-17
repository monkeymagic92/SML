package com.sml.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 프로그램명  : UserServiceImpl
 * 날짜       : 2022-01-09 / 일요일 / 오후 10:53
 * 설명       :
 */

@Service
@Transactional(rollbackFor=Exception.class)
public class UserServiceImpl {

	@Autowired
	private UserMapper mapper;

	public List<?> selUser(Map<String, Object> map) throws Exception {

		return mapper.selUser(map);

	}

	public void insertTr(HttpServletRequest request) {
		mapper.insertTr1();
	}



}
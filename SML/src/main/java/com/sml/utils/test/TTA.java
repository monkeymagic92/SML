package com.sml.utils.test;

import com.sml.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 프로그램명  : TTA
 * 날짜       : 2022-01-17 / 월요일 / 오전 10:16
 * 설명       :
 */

@Component
public class TTA {

	@Autowired
	public UserMapper mapper;

	@Scheduled(fixedRate=5000)
	public void sec() {

		System.out.println("--------------------------------------jytest");
		mapper.t_test();
		System.out.println("--------------------------------------jytest");
	}
}

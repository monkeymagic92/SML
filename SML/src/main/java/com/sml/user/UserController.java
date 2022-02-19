package com.sml.user;

import com.sml.utils.common.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 프로그램명  : UserController
 * 날짜       : 2022-02-13 / 일요일 / 오후 6:56
 * 설명       :
 */

@Controller
public class UserController extends CommonController {

	@Autowired
	private UserService service;
}

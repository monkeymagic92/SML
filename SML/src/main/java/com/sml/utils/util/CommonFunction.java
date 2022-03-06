package com.sml.utils.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 프로그램명  : CommonFunction
 * 날짜       : 2022-03-06 / 일요일 / 오후 8:01
 * 설명       :
 */
public class CommonFunction {

	public static String loginChk(HttpServletRequest request) {

		String result = "success";

		HttpSession session = request.getSession();
		Map<String, Object> userChk = (Map<String, Object>) session.getAttribute("userList");

		if(userChk != null) {
			result = "fail";
		}
		return result;
	}
}

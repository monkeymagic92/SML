package com.sml.user;

import com.sml.utils.common.CommonController;
import com.sml.utils.util.Bind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 프로그램명  : UserController
 * 날짜       : 2022-01-09 / 일요일 / 오후 10:52
 * 설명       :
 */

@Controller
public class UserController extends CommonController {

	@Autowired
	private UserServiceImpl service;

	@RequestMapping(value = "user/main", method = RequestMethod.GET)
	public String login(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {



		return "user/main";
	}

	@RequestMapping(value = "user/ajax", method = RequestMethod.POST)
	@ResponseBody
	public List<?> ajax(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		List<?> list = service.selUser(map);

		return list;
	}

	@RequestMapping(value = "user/insertTr", method = RequestMethod.GET)
	public String insertTr(HttpServletRequest request) {

		service.insertTr(request);

		return "user/insertTr";
	}
}

package com.sml.user;

import com.sml.utils.common.CommonController;
import com.sml.utils.util.CommonFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 프로그램명  : UserController
 * 날짜       : 2022-02-13 / 일요일 / 오후 6:56
 * 설명       :
 */

@Controller
public class UserController extends CommonController {

	@Autowired
	private UserService service;

	/**
	 * 로그인 첫 화면 (index)
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/loginReg")
	public String loginReg(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String result = CommonFunction.loginChk(request);

		if(result.equals("fail")) {		// 로그인이 안되어있다면 로그인페이지로
			return "/user/loginReg";

		} else {						// 로그인이 되어있다면 index 페이지로
			return "redirect:/temp/index";
		}
	}

	/**
	 * 회원가입
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/signUp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> signUp(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = service.signUp(model, request, response);
		return map;
	}

	/**
	 * 로그인
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/signIn", method = RequestMethod.POST)
	public String signIn(Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) throws Exception {

		Map<String, Object> map = service.selectUserInfo(model, request, response);

		if(map.get("message").equals("success")) {	// 로그인 성공
			logger.info("로그인성공");
			return "redirect:/temp/index";

		} else {									// 로그인 실패
			logger.info("로그인실패");
			ra.addFlashAttribute("message", map.get("message"));
			return "redirect:/user/loginReg";
		}
	}

	/**
	 * 로그아웃
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/logout")
	public String logout(Model model, HttpServletRequest request, HttpSession session) throws Exception {

		logger.info("로그아웃");
		session.invalidate();

		return "redirect:/user/loginReg";
	}
}

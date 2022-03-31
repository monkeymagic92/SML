package com.sml.quote;

import com.sml.utils.common.CommonController;
import com.sml.utils.util.Bind;
import com.sml.utils.util.ViewRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 날짜       : 2022-01-01
 * 설명       : 시세관련 Controller
 */

@Controller
public class QuoteController extends CommonController {

	@Autowired
	private QuoteService service;

	@RequestMapping(value = "/quote/thHour", method = RequestMethod.GET)
	public String selectThHour(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// thHour.jsp에 들어갈 model
		model.addAttribute("list", service.selectQuote());

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "3th Hour Table");
		model.addAttribute("subCntn", "갱신일자 : " + service.selectThKRWUpdDt());

		model.addAttribute("view", "/quote/thQuote");

		return ViewRef.LAYOUT_TEMP;
	}

	// 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨
	// 테스트용으로 클릭버튼 눌렀을때 DB에 코인업데이트내용 저장되게 하는 controller
	@RequestMapping(value = "/quote/insertCoinList", method = RequestMethod.POST)
	public void insertCoinList(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		service.insertCoinList();
	}

	@RequestMapping(value = "/quote/test", method = RequestMethod.POST)
	public void test(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		service.test(request, response);
	}












	@RequestMapping(value = "/quote/ajax")
	public void ajaxIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("--jytest--");
		System.out.println(systemProp.getProperty("db.maria.url"));
		System.out.println("--jytest--");


		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		model.addAttribute("message", "Controller 에러 modl");
		//throw new BusinessException("Controller");

		//service.ajaxIndex(model, request, response);
	}
}

package com.sml.quote;

import com.sml.utils.common.CommonController;
import com.sml.utils.util.ViewRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 날짜       : 2022-01-01
 * 설명       : 시세관련 Controller..
 */

@Controller
public class QuoteController extends CommonController {

	@Autowired
	private QuoteService service;

	@RequestMapping(value = "/quote/quote_AM10", method = RequestMethod.GET)
	public String selectThHour(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// thHour.jsp에 들어갈 model
		model.addAttribute("list", service.selectCoinThQuote());

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "3th Hour Table");
		model.addAttribute("subCntn", "갱신일자 : " + service.selectThKRWUpdDt());

		model.addAttribute("view", "/quote/quote_AM10");

		return ViewRef.LAYOUT_TEMP;
	}

	// 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨
	// 테스트용으로 클릭버튼 눌렀을때 DB에 코인업데이트내용 저장되게 하는 controller
	@RequestMapping(value = "/quote/insertCoinList", method = RequestMethod.POST)
	public void insertCoinList(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		service.insertCoinList();
	}

}

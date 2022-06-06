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

	/********************** 10:00am (RACE) 코인 Start ************************/
	@RequestMapping(value = "/quote/quote_race", method = RequestMethod.GET)
	public String coinRaceIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 10:00am (RACE) 데이터 list
		model.addAttribute("list", service.selectCoinRaceList());

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "Race");
		model.addAttribute("subCntn", "최근갱신일자 : " + service.selectCoinRaceUpdDt());

		model.addAttribute("view", "/quote/quote_race");

		return ViewRef.LAYOUT_TEMP;
	}

	// 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨
	// 테스트용으로 클릭버튼 눌렀을때 DB에 코인업데이트내용 저장되게 하는 controller
	@RequestMapping(value = "/quote/insertCoinRaceList", method = RequestMethod.POST)
	public void insertSchRaceCoin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 버튼 클릭시 테스트 insert 코인
		service.insertCoinRaceTest();
	}
	/********************** 10:00am (RACE) 코인 End ************************/



	/********************** 08:55am (DAY) 코인 Start ************************/
	@RequestMapping(value = "/quote/quote_day", method = RequestMethod.GET)
	public String coinDayIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 08:55am (DAY) 데이터 list
		model.addAttribute("list", service.selectCoinDayList());

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "Day");
		model.addAttribute("subCntn", "최근갱신일자 : " + service.selectCoinDayUpdDt());

		model.addAttribute("view", "/quote/quote_day");

		return ViewRef.LAYOUT_TEMP;
	}

	// 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨
	// 테스트용으로 클릭버튼 눌렀을때 DB에 코인업데이트내용 저장되게 하는 controller
	@RequestMapping(value = "/quote/insertCoinDayList", method = RequestMethod.POST)
	public void insertSchDayCoin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 버튼 클릭시 테스트 insert 코인
		service.insertCoinDayTest();
	}
	/********************** 08:55am (DAY) 코인 End ************************/

}

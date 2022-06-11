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

	// **************************** (Race) 코인 Start ****************************
	@RequestMapping(value = "/quote/quote_race", method = RequestMethod.GET)
	public String coinRaceIndex(Model model, HttpServletRequest request, HttpServletResponse response, QuoteVO vo) throws Exception {

		// (Race) 10:00am데이터 list
		model.addAttribute("list", service.selectCoinRaceList(vo));

		System.out.println("--jytest--");
		System.out.println(vo.getSearchMARKET());
		System.out.println("--jytest--");

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "10:00AM Table");
		model.addAttribute("subCntn", "최근갱신일자 : " + service.selectCoinRaceUpdDt());

		model.addAttribute("view", "/quote/quote_race");

		return ViewRef.LAYOUT_TEMP;
	}

	// (Race) 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨 (ajax Test버튼 눌렀을때 insert용)
	@RequestMapping(value = "/quote/insertRaceCoinList", method = RequestMethod.POST)
	public void insertSchRaceCoin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 버튼 클릭시 테스트 insert 코인
		service.insertCoinRaceTest();
	}
	// **************************** (Race) 코인 End ****************************



	// **************************** (Day) 코인 Start ****************************
	@RequestMapping(value = "/quote/quote_day", method = RequestMethod.GET)
	public String coinDayIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// (Day) 08:55am 데이터 list
		model.addAttribute("list", service.selectCoinDayList());

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "08:55AM Table");
		model.addAttribute("subCntn", "최근갱신일자 : " + service.selectCoinDayUpdDt());

		model.addAttribute("view", "/quote/quote_day");

		return ViewRef.LAYOUT_TEMP;
	}

	// (Day) 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨  (ajax Test버튼 눌렀을때 insert용)
	@RequestMapping(value = "/quote/insertDayCoinList", method = RequestMethod.POST)
	public void insertSchDayCoin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		service.insertCoinDayTest();
	}
	// **************************** (Day) 코인 End ****************************

}






































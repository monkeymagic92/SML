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
 * 설명       : 시세관련 Controller..
 */

@Controller
public class QuoteController extends CommonController {

	@Autowired
	private QuoteService service;

	// **************************** (Race) 코인 Start ****************************
	// (Race) 코인 index 페이지
	@RequestMapping(value = "/quote/quote_race", method = RequestMethod.GET)
	public String coinRaceIndex(Model model, HttpServletRequest request, HttpServletResponse response, QuoteVO vo) throws Exception {

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "Race");
		model.addAttribute("subCntn", "최근갱신일자 : " + service.selectCoinRaceUpdDt());

		model.addAttribute("view", "/quote/quote_race");

		return ViewRef.LAYOUT_TEMP;
	}

	// (Race) 코인 조회
	@RequestMapping(value = "/quote/selectQuoteRace", method = RequestMethod.GET)
	public void selectCoinRaceList(Model model, HttpServletRequest request, HttpServletResponse response, QuoteVO vo) throws Exception {
		service.selectCoinRaceList(model, request, response, vo);
	}

	// (Race) 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨 (ajax Test버튼 눌렀을때 insert용)
	@RequestMapping(value = "/quote/insertRaceCoinList", method = RequestMethod.POST)
	public void insertSchRaceCoin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 버튼 클릭시 테스트 insert 코인
		service.insertCoinRaceTest();
	}
	// **************************** (Race) 코인 End ****************************



	// **************************** (Day) 코인 Start ****************************
	// (Day) 코인 index 페이지
	@RequestMapping(value = "/quote/quote_day", method = RequestMethod.GET)
	public String coinDayIndex(Model model, HttpServletRequest request, HttpServletResponse response, QuoteVO vo) throws Exception {

		// indexTemp.jsp에 들어갈 model
		model.addAttribute("title", "Day");
		model.addAttribute("subCntn", "최근갱신일자 : " + service.selectCoinDayUpdDt());

		model.addAttribute("view", "/quote/quote_day");

		return ViewRef.LAYOUT_TEMP;
	}

	// (Day) 코인 조회
	@RequestMapping(value = "/quote/selectQuoteDay", method = RequestMethod.GET)
	public void selectCoinDayList(Model model, HttpServletRequest request, HttpServletResponse response, QuoteVO vo) throws Exception {
		service.selectCoinDayList(model, request, response, vo);
	}

	// (Day) 나중에 스케줄러 돌리면 해당 부분은 삭제해도됨  (ajax Test버튼 눌렀을때 ins`ert용)
	@RequestMapping(value = "/quote/insertDayCoinList", method = RequestMethod.POST)
	public void insertSchDayCoin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		service.insertCoinDayTest();
	}
	// **************************** (Day) 코인 End ****************************

}






































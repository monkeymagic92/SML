package com.sml.pump;

import com.sml.quote.QuoteVO;
import com.sml.utils.common.CommonController;
import com.sml.utils.util.Bind;
import com.sml.utils.util.ViewRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Map;

/**
 * 날짜       : 2022-06-16 / 목요일 / 오후 6:58
 * 설명       : 일정 상승률 이상을 기록한 코인기록
 */

@Controller
public class PumpController extends CommonController {

	@Autowired
	private PumpService service;

	@RequestMapping(value = "/pump/pumpPopup", method = RequestMethod.GET)
	public String pumpModalIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		if(map.get("TYPE").equals("RACE")) {
			model.addAttribute("title", service.selectPumpRaceHeader(map));	// (RACE) 팝업창 헤더 데이터
			model.addAttribute("list", service.selectPumpRaceList(map));		// 팝업창 데이터 list

		} else if(map.get("TYPE").equals("DAY")) {
			model.addAttribute("title", service.selectPumpDayHeader(map));		// (DAY) 팝업창 헤더 데이터
			model.addAttribute("list", service.selectPumpDayList(map));			// 팝업창 데이터 list
		}

		return "/pump/pumpPopup";
	}




	// ------------------------------------------------------------------------------------------------------------

	@RequestMapping (value="/pump/saveTest")
	public void saveTest(ModelMap model, HttpServletRequest request, HttpServletResponse response, Writer out) throws Exception {
		service.saveTest(model, request, response, out);
	}

	// pump 트랜잭션 및 ajax 테스트
	@RequestMapping (value="/pump/selectTest")
	public void selectTest(ModelMap model, HttpServletRequest request, HttpServletResponse response, Writer out) throws Exception {
		service.selectTest(model, request, response, out);
	}
	// ------------------------------------------------------------------------------------------------------------

}

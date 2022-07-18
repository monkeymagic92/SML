package com.sml.api;

import com.sml.quote.QuoteVO;
import com.sml.utils.common.CommonController;
import com.sml.utils.common.CommonService;
import com.sml.utils.util.ViewRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 날짜       : 2022-07-15 / 금요일 / 오후 9:09
 * 설명       : 내가산 코인 목록 관련 API
 */

@Controller
public class UpbitMyListController extends CommonController {

	@Value("#{systemProp['upbit.ackey']}")
	private String ackey;

	@Value("#{systemProp['upbit.sekey']}")
	private String sekey;

	public String getAckey() { return ackey; }

	public String getSekey() {
		return sekey;
	}

	@Autowired
	private UpbitMyListService service;

	@RequestMapping(value = "/api/upbitMyListTest", method = RequestMethod.GET)
	public String coinMyListIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		model.addAttribute("view", "/test/coinMyListTest");
		return ViewRef.LAYOUT_TEMP;
	}

	@RequestMapping(value = "/api/insertCoinMyList")
	public void insertCoinMyList(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		service.updateCoinMyList(model, request, response);

	}

}

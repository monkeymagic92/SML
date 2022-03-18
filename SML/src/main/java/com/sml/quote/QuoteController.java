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

		model.addAttribute("list", service.selectQuote());
		model.addAttribute("title", "3th Hour Table");
		model.addAttribute("view", "/quote/thHour");

		return ViewRef.LAYOUT_TEMP;
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

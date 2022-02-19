package com.sml.quote;

import com.sml.utils.common.CommonController;
import com.sml.utils.core.BusinessException;
import com.sml.utils.util.Bind;
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

	@RequestMapping(value = "/quote/index", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

		model.addAttribute("list", service.selectQuote());

		return "/quote/index";
	}

	@RequestMapping(value = "/quote/ajax")
	public void ajaxIndex(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		throw new BusinessException("Controller");

		//service.ajaxIndex(model, request, response);
	}


}

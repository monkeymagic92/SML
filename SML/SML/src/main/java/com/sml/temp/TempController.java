package com.sml.temp;

import com.sml.utils.util.ViewRef;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 프로그램명  : TempController
 * 날짜       : 2022-03-18 / 금요일 / 오전 11:50
 * 설명       : 초기화면 Temp
 */

@Controller
public class TempController {

	@RequestMapping(value = "/temp/index", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		model.addAttribute("title", "TempIndex");
		model.addAttribute("view", "/temp/index");

		return ViewRef.LAYOUT_TEMP;
	}

}

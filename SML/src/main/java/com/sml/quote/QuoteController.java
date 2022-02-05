package com.sml.quote;

import com.sml.utils.common.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 날짜       :
 * 설명       :
 */

@Controller
public class QuoteController extends CommonController {

	@Autowired
	private QuoteService service;

}

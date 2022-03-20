package com.sml.quote;

import com.sml.api.UpbitAPIService;
import com.sml.utils.util.Bind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 날짜       : 2022-01-01
 * 시스템      : 시세조회
 * 설명        : 각 코인 시세애 대한 비니지스 로직
 */

@Service
public class QuoteService {

	@Autowired
	private QuoteMapper mapper;

	@Autowired
	private UpbitAPIService upbitAPI;


	public List<?> selectQuote() throws Exception {
		List<?> list = mapper.selectQuote();
		return list;
	}

	/*
		파라미터(KRW / BTC에 따라 해당 값을 가져온다
		스케줄러 ( 10초에 한번씩 자동실행, (1000ms)
	 */
//	@Scheduled(fixedRate=10000)
	public void insertCoinList() throws Exception {
		upbitAPI.insertCoinList("KRW");
	}


































	public void ajaxIndex(Model model, HttpServletRequest request, HttpServletResponse response, Writer out) throws Exception {

		Bind bind = new Bind(request);

		Map<String, Object> map = bind.getDto();
		//throw new BusinessException("service");

		//CommonUtil.jsonResponse(response, map);
	}
}

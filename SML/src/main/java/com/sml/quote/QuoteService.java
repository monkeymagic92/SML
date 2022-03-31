package com.sml.quote;

import com.sml.api.UpbitAPIService;
import com.sml.utils.common.CommonService;
import com.sml.utils.core.BusinessException;
import com.sml.utils.util.Bind;
import com.sml.utils.util.CommonUtil;
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
public class QuoteService extends CommonService {

	@Autowired
	private QuoteMapper mapper;

	@Autowired
	private UpbitAPIService upbitAPI;


	public List<?> selectQuote() throws Exception {
		List<?> list = mapper.selectQuote();
		return list;
	}

	// (3시간) 갱신일자(UPD_DT) 값 가져오기
	public String selectThKRWUpdDt() {

		return mapper.selectThKRWUpdDt();
	}

	/*
		파라미터(KRW / BTC에 따라 해당 값을 가져온다
		스케줄러 ( 10초에 한번씩 자동실행, (1000ms)
	 */
	//@Scheduled(fixedRate=20000)
	public void insertCoinList() throws Exception {
		upbitAPI.insertCoinList("KRW");
		upbitAPI.insertCoinList("BTC");
	}

	public void test(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();
		map.put("AAA", "AAA");

		String a = "aaa";
		if(a.equals("aaa")) {
			throw new BusinessException("에러발생");
		}

		CommonUtil.jsonResponse(response, map);
	}


































	public void ajaxIndex(Model model, HttpServletRequest request, HttpServletResponse response, Writer out) throws Exception {

		Bind bind = new Bind(request);

		Map<String, Object> map = bind.getDto();
		//throw new BusinessException("service");

		//CommonUtil.jsonResponse(response, map);
	}
}

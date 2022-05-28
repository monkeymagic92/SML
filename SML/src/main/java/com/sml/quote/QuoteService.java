package com.sml.quote;

import com.sml.api.UpbitAPIService;
import com.sml.utils.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


	public List<?> selectCoinThQuote() throws Exception {
		List<?> list = mapper.selectQuoteKRWList();
		return list;
	}

	/**
	 * (3시간) 갱신일자(UPD_DT) 값 가져오기.
	 * @return
	 */
	public String selectThKRWUpdDt() {
		return mapper.selectThKRWUpdDt();
	}


	/**
	 * thQuote.jsp - click Test Ajax버튼 눌렀을때 테스트용 함수
	 * @throws Exception
	 */
	public void insertCoinList() throws Exception {

		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW"); // 코인 가격 저장하는 QUOTE 테이블
		upbitAPI.insertCoin10KRW(listMap);

	}

	/**
		Scheduled : 매일 오전 10:00분
		경주마 뛰고난후 오전10시에 시작가 - 고가 최대 몇% 올랐는지 통계낸다
	 */
	@Scheduled(cron = "0 00 10 * * *")
	public void updateRaceBeforeCoinList() throws Exception {

		List<Map<String, Object>> listMap = new ArrayList<>();

		listMap = upbitAPI.insertCoinList("KRW");		// 코인 가격 저장하는 QUOTE 테이블
		upbitAPI.insertCoin10KRW(listMap);				// 실제 10시 코인 테이블

	}
}

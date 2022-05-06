package com.sml.quote;

import com.sml.api.UpbitAPIService;
import com.sml.utils.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
	 	나중에 스케쥴러 매 3시간update하는 로직 넣기
	 */
	//@Scheduled(fixedRate=20000)
	public void insertCoinList() throws Exception {
		Map<String, Object> map = new HashMap<>();

		upbitAPI.insertCoinList("KRW");		// 어떤 3시간 6시간 경주마든 어떤테이블이든 기본으로 값 가져오는 Temp 테이블이라 보면됨
		//mapper.insertBeforeRaceList(map);	// Temp테이블에 있는데이터를 경주마테이블에 넣는다
	}

	/**
		Scheduled : 매일 오전 08:55분
		경주마 뛰기전 코인 리스트의 값을 저장한다
	 */
	@Scheduled(cron = "0 55 08 * * *")
	public void updateRaceBeforeCoinList() throws Exception {
		Map<String, Object> map = new HashMap<>();

//		upbitAPI.insertCoinList("KRW");
//		mapper.insertBeforeRaceList(map);	// Temp테이블에 있는데이터를 경주마테이블에 넣는다

	}
}

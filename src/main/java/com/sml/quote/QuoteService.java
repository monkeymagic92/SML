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

	/************************ 10:00am (RACE) 코인 Start ************************/
	// 10:00am (RACE) 코인 조회
	public List<?> selectCoinRaceList() throws Exception {
		List<?> list = mapper.selectCoinRaceList();
		return list;
	}

	// 10:00am (RACE) 코인 갱신일자
	public String selectCoinRaceUpdDt() {
		return mapper.selectCoinRaceUpdDt();
	}

	// 10:00am (RACE) 코인 저장
	public void insertCoinRace(List<Map<String, Object>> listMap) {
		mapper.insertCoinRace(listMap);
	}


	// 10:00am (RACE) 코인 테스트 ajax 저장 (추후 테스트 다끝나면 삭제)
	public void insertCoinRaceTest() throws Exception {

		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW"); // 코인 가격 저장하는 QUOTE 테이블
		insertCoinRace(listMap);				  // 당일 오전10시에 코인가격 insert
	}

	// 10:00am (RACE) 코인 저장 스케줄러	( 오전9시 장이 시작되고 10시까지의 시가 - 고가를 저장한다 )
	@Scheduled(cron = "0 00 10 * * *")
	public void insertSchRaceCoin() throws Exception {

		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW");	// 코인 가격 저장하는 QUOTE 테이블
		insertCoinRace(listMap);					// 당일 오전10시에 코인가격 insert
	}
	/************************ 10:00am (RACE) 코인 End ************************/



	/************************ 08:55am (DAY) 코인 Start ************************/
	// DAY 코인 조회
	public List<?> selectCoinDayList() throws Exception {
		List<?> list = mapper.selectCoinDayList();
		return list;
	}

	// DAY 코인 갱신일자
	public String selectCoinDayUpdDt() {
		return mapper.selectCoinDayUpdDt();
	}

	// DAY 코인 저장
	public void insertCoinDay(List<Map<String, Object>> listMap) {
		mapper.insertCoinDay(listMap);
	}

	// DAY 코인 테스트 ajax 저장 (추후 테스트 다끝나면 삭제)
	public void insertCoinDayTest() throws Exception {

		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW"); // 코인 가격 저장하는 QUOTE 테이블
		insertCoinDay(listMap);				  	  // 당일 23:59pm 코인가격 insert
	}

	// DAY 코인 저장 스케줄러  ( 그날하루 마감전까지 최대 시가, 고가를 저장한다 )
	@Scheduled(cron = "0 55 08 * * *")
	public void insertSchDayCoin() throws Exception {

		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW");	// 코인 가격 저장하는 QUOTE 테이블
		insertCoinDay(listMap);						// 당일 23:59pm 코인가격 insert
	}
	/************************ 23:59pm (DAY) 코인 End ************************/

}

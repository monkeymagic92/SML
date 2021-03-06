package com.sml.quote;

import com.sml.api.UpbitAPIService;
import com.sml.pump.PumpService;
import com.sml.utils.common.CommonService;
import com.sml.utils.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

	@Autowired
	private PumpService pumpService;

	// **************************** (Race) 코인 Start ****************************
	/**
	 * (Race) 코인 리스트
	 * @return
	 * @throws Exception
	 */
	public void selectCoinRaceList(Model model, HttpServletRequest request, HttpServletResponse response, QuoteVO vo) throws Exception {
		List<?> list = mapper.selectCoinRaceList(vo);
		CommonUtil.jsonResponse(response, list);
	}

	/**
	 *  (Race) 갱신일자(UPD_DT) 값 가져오기.
	 * @return
	 */
	public String selectCoinRaceUpdDt() {
		return mapper.selectCoinRaceUpdDt();
	}

	/**
	 * (Race) 오전10시 코인가격를 10시테이블에 INSERT
	 * @param listMap
	 */
	public void insertCoinRace(List<Map<String, Object>> listMap) {
		mapper.insertCoinRace(listMap);
	}

	/**
	 * quote_race.jsp - click Test Ajax버튼 눌렀을때 테스트용 함수
	 * @throws Exception
	 */
	public void insertCoinRaceTest() throws Exception {
		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW");
		insertCoinRace(listMap);
		pumpService.insertPump(listMap, 20, "RACE");
	}

	/**
		Scheduled : 매일 오전 10:00분
		경주마 뛰고난후 오전10시에 시작가 - 고가 최대 몇% 올랐는지 통계낸다
	 */
	@Scheduled(cron = "0 00 10 * * *")
	public void insertSchRaceCoin() throws Exception {
		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW");	// 코인 가격 저장하는 QUOTE 테이블
		insertCoinRace(listMap);					// 당일 오전10시에 코인가격 insert
		pumpService.insertPump(listMap, 15, "RACE");
	}
	// **************************** (Race) 코인 End ****************************



	// **************************** (Day) 코인 Start ****************************
	/**
	 * (Day) 코인 리스트
	 * @return
	 * @throws Exception
	 */
	public void selectCoinDayList(Model model, HttpServletRequest request, HttpServletResponse response, QuoteVO vo) throws Exception {
		List<?> list = mapper.selectCoinDayList(vo);
		CommonUtil.jsonResponse(response, list);
	}

	/**
	 *  갱신일자(UPD_DT) 값 가져오기.
	 * @return
	 */
	public String selectCoinDayUpdDt() {
		return mapper.selectCoinDayUpdDt();
	}

	/**
	 * Day(08:55am) 코인가격를 10시테이블에 INSERT
	 * @param listMap`
	 */
	public void insertCoinDay(List<Map<String, Object>> listMap) {
		mapper.insertCoinDay(listMap);
	}

	/**
	 * quote_day.jsp - Test Ajax버튼 눌렀을때 Day(08:55am) 테스트용 함수
	 * @throws Exception
	 */
	public void insertCoinDayTest() throws Exception {
		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW");
		insertCoinDay(listMap);
		pumpService.insertPump(listMap, 15, "DAY");
	}

	/**
	 * Scheduled : 매일 오전 08:55분
	 */
	@Scheduled(cron = "0 55 08 * * *")
	public void insertSchDayCoin() throws Exception {
		List<Map<String, Object>> listMap = new ArrayList<>();
		listMap = upbitAPI.insertCoinList("KRW");
		insertCoinDay(listMap);
		pumpService.insertPump(listMap, 20, "DAY");
	}

	/**
	 * 상승률 1~5위 코인 리스트를 Header에 표기
	 * @return
	 */
	public List<?> selectCoinDayRank() {
		return mapper.selectCoinDayRank();
	}
	// **************************** (Day) 코인 End ****************************

}

package com.sml.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sml.utils.common.CommonService;
import com.sml.utils.util.StringUtil;
import com.sml.utils.util.TimeMaximum;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 프로그램명  : UpbitAPIService
 * 날짜       : 2022-03-20 / 일요일 / 오후 1:50
 * 설명       :
 */

@Service
public class UpbitAPIService extends CommonService {

	@Autowired
	private UpbitAPIMapper mapper;

	/**
	 * 코인 전체리스트와 코인 시세정보를 가져온다
	 * @throws Exception
	 */
	public List<Map<String, Object>> insertCoinList(String market) throws Exception {

		// 현재시간 날짜 가져오기 ( YYYY-MM-DD HH24:MI:SS ) ( 오라클 클라우드 SYSDATE
		String upd_dt = TimeMaximum.nowDate();

		// 1. coin리스트를 List<Map>에 담는다
		List<Map<String, Object>> coinList = getUpbitCoinList(market);

		// 1차적으로 merge in to 해서 코인정보를 전체 insert ( mybatis에서 forEach 사용 (batch, INSERT 속도 향상 )
		if(market.equals("KRW")) {
			mapper.insertCoinListKRW(coinList);
		} else {
			mapper.insertCoinListBTC(coinList);
		}

		// 리스트맵에서 market키값을 가져온다
		String str = parsingJsonToString(coinList, "MARKET");

		// 마지막 쉼표제거를 위해 replace
		String strReplace = StringUtil.lastStringDelete(str);

		/*
			■ 아래 글은 내일 작업할때 꼭 보고 작업하기 중요한 내용임, 분(minute)봉 기준으로 8:59 ~ 10:00 데이터 api 추출후 해당 정볼를 RACE테이블에 insert, update 하기

			■ 08:59분에는 분(minute)봉 기준으로 '저가'를 update하고 10:00에는 일(day)봉 기준으로 '고가'를 update하기
			( 분봉 기준으로 하면 09:10분에 최고점찍고 하락갔을때 10시기준으로 보면 10시 '분'봉에 고점이 찍히기에 정확한 데이터 통계가 안됨
			그래서 '일'봉 기준으로 잡아야 그날 10시까지 '고점' 데이터를 가져올수 있음 )

			■ 분봉 테이블, 일봉 테이블 HIS로 2개 만들기 그리고 계속 거기다가 UPDATE 누적하기
			T_COIN_QUOTE_KRW 테이블은 단순 코인 시세를 받아오는 테이블이고 각 상황에따라 분,일봉 HIS 테이블에 계속 update 치기

			(T_COIN_QUOTE_KRW = 분봉HIS : 일봉HIS) 테이블로 누적한다고 생각하면 됨
			그리고 제일위에 내용대로 분봉 '저가' 일봉 '고가'는 각 테이블끼리 서로 MRAKET을 JOIN 하기

			strReplace가 코인 이름 가지고있음 ex)KRW-BTC
			즉, if문 분기처리하여 coinQuoteList < 변수에다가 분봉일경우 분봉 API 실행하여 데이터 가져오기
			해당 데이터는 getCoinQuoteList() 함수처럼 간단하게 값 가져오는 코드 참고하면 됨
			그리고 JSONParser parser = new JSONParser(); 사용해서 for 반복문 돌려서 데이터 들고오면됨
			반복문 또한 함수처리하여 작성해보기
			다시말해 내가 나중에 알아볼수있게 간단하게 코딩하기 ( 현재 너무 복잡함 )

		 */

		// 코인가격이 배열로 JSON형태의 값을 가지고 온다  (strReplace = KRW-BTC 형태)
		String coinQuoteList = getCoinQuoteList(strReplace);

		// 코인시세(quote) 파싱작업
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(coinQuoteList);
		JSONArray jsonArr = (JSONArray)obj;

		List<Map<String, Object>> listMap = new ArrayList<>();

		for(int i=0; i<jsonArr.size(); i++) {
			JSONObject jsonObj = (JSONObject)jsonArr.get(i);

			Map<String, Object> map = new HashMap<>();

			map.put("MARKET", jsonObj.get("market"));
			map.put("TRADE_DATE", jsonObj.get("trade_date"));
			map.put("TRADE_TIME", jsonObj.get("trade_time"));
			map.put("TRADE_DATE_KST", jsonObj.get("trade_date_kst"));
			map.put("TRADE_TIME_KST", jsonObj.get("trade_time_kst"));
			map.put("TRADE_TIMESTAMP", jsonObj.get("trade_timestamp"));
			map.put("OPENING_PRICE", jsonObj.get("opening_price"));
			map.put("HIGH_PRICE", jsonObj.get("high_price"));
			map.put("LOW_PRICE", jsonObj.get("low_price"));
			map.put("TRADE_PRICE", jsonObj.get("trade_price"));
			map.put("PREV_CLOSING_PRICE", jsonObj.get("prev_closing_price"));
			map.put("CHANGE", jsonObj.get("change"));
			map.put("CHANGE_PRICE", jsonObj.get("change_price"));
			map.put("CHANGE_RATE", jsonObj.get("change_rate"));
			map.put("SIGNED_CHANGE_PRICE", jsonObj.get("signed_change_price"));
			map.put("SIGNED_CHANGE_RATE", jsonObj.get("signed_change_rate"));
			map.put("TRADE_VOLUME", jsonObj.get("trade_volume"));
			map.put("ACC_TRADE_PRICE", jsonObj.get("acc_trade_price"));
			map.put("ACC_TRADE_PRICE_24H", jsonObj.get("acc_trade_price_24h"));
			map.put("ACC_TRADE_VOLUME", jsonObj.get("acc_trade_volume"));
			map.put("ACC_TRADE_VOLUME_24H", jsonObj.get("acc_trade_volume_24h"));
			map.put("HIGHEST_52_WEEK_PRICE", jsonObj.get("highest_52_week_price"));
			map.put("HIGHEST_52_WEEK_DATE", jsonObj.get("highest_52_week_date"));
			map.put("LOWEST_52_WEEK_PRICE", jsonObj.get("lowest_52_week_price"));
			map.put("LOWEST_52_WEEK_DATE", jsonObj.get("lowest_52_week_date"));
			map.put("TIMESTAMP", jsonObj.get("timestamp"));
			map.put("UPD_DT", upd_dt);

			listMap.add(map);

		}

		// 코인 (일) 시세정보 log
//		for(int i=0; i<listMap.size(); i++) {
//			logger.info(i + " : " + listMap.get(i));
//		}


		if(market.equals("KRW")) {
			mapper.updateCoinQuoteKRW(listMap);	//( mybatis에서 forEach 돌리기 )
			//mapper.insertCoinQuoteKRW_HIS(listMap); // KRW_HIS 테이블
		} else {
			mapper.updateCoinQuoteBTC(listMap);
			//mapper.insertCoinQuoteBTC_HIS(listMap); // KRW_BTC 테이블
		}

		return listMap;
	}


	/**
	 * 업비트 코인 전체 리스트 가져오기
	 * @param coinCode
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> getUpbitCoinList(String coinCode) throws IOException {

		List<Map<String, Object>> listKRWMap = new ArrayList<>();
		List<Map<String, Object>> listBTCMap = new ArrayList<>();

		HttpsURLConnection conn = null;
		BufferedReader rd = null;

		try {
			int CONNECT_TIMEOUT_VALUE = 5000; // URL 연결 실패 시 시간설정(5초)
			int READ_TIMEOUT_VALUE = 5000;    // 데이터 조회 시간설정(5초)

			StringBuilder urlBuilder = new StringBuilder("https://api.upbit.com/v1/market/all?isDetails=false"); //URL
			//urlBuilder.append("?" + URLEncoder.encode("LoginID", "UTF-8") + "=" + URLEncoder.encode(LoginID, "UTF-8"));

			URL url = new URL(urlBuilder.toString());

			conn = (HttpsURLConnection) url.openConnection();

			conn.setConnectTimeout(CONNECT_TIMEOUT_VALUE);	//서버 접속시 연결 시간
			conn.setReadTimeout(READ_TIMEOUT_VALUE);		//연결 후 데이터 가져오는 시간
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			int code = conn.getResponseCode();

			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			} else {

				if(conn.getAllowUserInteraction() == true){
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(("").getBytes())));
				}
			}

			StringBuilder sb = new StringBuilder();
			String line;
			String result = "";

			if ((line = rd.readLine()) != null && !"".equals(line)) {
				sb.append(line);
			}

			if(sb.length() > 0) {
				result += line;
			}
			// ---------------- Json Array 파싱법 (배열key 값이 없는 일반 배열로 감싸져있는 json배열 파싱방법)
			JsonParser parser = new JsonParser();
			JsonArray jsonArray = (JsonArray) parser.parse(result);

			for(int i=0; i<jsonArray.size(); i++) {

				JsonObject allCoin = (JsonObject) jsonArray.get(i);

				Map<String, Object> krwMap = new HashMap<>();
				Map<String, Object> btcMap = new HashMap<>();

				String market = allCoin.get("market").getAsString();
				String korean_name = allCoin.get("korean_name").getAsString();
				String english_name = allCoin.get("english_name").getAsString();

				// 파라미터가 KRW일경우 KRW 마켓에 코인리스트만 들고온다
				if(coinCode.equals("KRW")) {

					if(market.substring(0, 3).equals("KRW")) {
						krwMap.put("MARKET", market);
						krwMap.put("KOR_NM", korean_name);
						krwMap.put("ENG_NM", english_name);

						listKRWMap.add(krwMap);
					}

					// 파라미터가 BTC일경우 BTC 마켓에 코인리스트만 들고온다
				} else if(coinCode.equals("BTC")) {

					if(market.substring(0, 3).equals("BTC")) {
						btcMap.put("MARKET", market);
						btcMap.put("KOR_NM", korean_name);
						btcMap.put("ENG_NM", english_name);

						listBTCMap.add(btcMap);
					}
				}
			}

			// jsonArray 전체값 출력
			//System.out.println(jsonArray);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if(rd != null){
				rd.close();
			}
			if(conn != null) {
				conn.disconnect();
			}
		}

		if(coinCode.equals("KRW")) {
			return listKRWMap;
		} else {
			return listBTCMap;
		}
	}


	/**
	 * 특정코인시세를 Json형태로 가져옴
	 * @param str
	 * @return
	 */
	public String getCoinQuoteList(String str) {
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String body = "";

		HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> responseEntity = rest.exchange("https://api.upbit.com/v1/ticker?markets="+str, HttpMethod.GET, requestEntity, String.class);
		HttpStatus httpStatus = responseEntity.getStatusCode();
		int status = httpStatus.value();
		String response = responseEntity.getBody();

//        System.out.println("Response status: " + status);
//        System.out.println(response);

		return response;
	}


	/**
	 * List<Map>에 특정Key값을 ',' 구분으로 뽑아온다  (StringUtil.lastStringDelete(str) 함수랑 같이사용
	 * @param listMap
	 * @param val
	 * @return
	 */
	public String parsingJsonToString(List<Map<String, Object>> listMap, String val) {

		String str = "";
		for (int i = 0; i < listMap.size(); i++) {
			str += listMap.get(i).get(val) + ",";
		}
		return str;
	}

}
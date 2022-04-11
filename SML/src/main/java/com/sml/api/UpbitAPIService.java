package com.sml.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sml.utils.util.StringUtil;
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
public class UpbitAPIService {

	@Autowired
	private UpbitAPIMapper mapper;

	/**
	 * 코인 전체리스트와 코인 시세정보를 가져온다
	 * @throws Exception
	 */
	public void insertCoinList(String market) throws Exception {

		// 1. coin리스트를 List<Map>에 담는다
		List<Map<String, Object>> coinList = getUpbitCoinList(market);

		// 1차적으로 merge in to 해서 코인정보를 다 insert한후
		for(int i=0; i<coinList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("MARKET", coinList.get(i).get("MARKET"));
			map.put("KOR_NM", coinList.get(i).get("KOR_NM"));
			map.put("ENG_NM", coinList.get(i).get("ENG_NM"));

			if(market.equals("KRW")) {
				mapper.insertCoinListKRW(map);
			} else {
				mapper.insertCoinListBTC(map);
			}
		}

		// 리스트맵에서 market키값을 가져온다
		String str = parsingJsonToString(coinList, "MARKET");

		// 마지막 쉼표제거를 위해 replace
		String strReplace = StringUtil.lastStringDelete(str);

		// 코인가격이 배열로 JSON형태의 값을 가지고 온다
		String coinQuoteList = getCoinQuoteList(strReplace);

		// 코인시세(quote) 파싱작업
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(coinQuoteList);
		JSONArray jsonArr = (JSONArray)obj;

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

			if(market.equals("KRW")) {
				mapper.updateCoinQuoteKRW(map);
				mapper.insertCoinQuoteKRW_HIS(map); // KRW_HIS 테이블
			} else {
				mapper.updateCoinQuoteBTC(map);
				mapper.insertCoinQuoteBTC_HIS(map); // KRW_BTC 테이블
			}
		}
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

				// 파라미터가 BTC일경우 BTC마켓에 코인리스트만 들고온다
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

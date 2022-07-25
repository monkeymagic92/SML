package com.sml.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sml.quote.QuoteVO;
import com.sml.utils.common.CommonController;
import com.sml.utils.common.CommonService;
import com.sml.utils.util.TimeMaximum;
import com.sml.utils.util.ViewRef;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 날짜       : 2022-07-15 / 금요일 / 오후 9:09
 * 설명       : 내가산 코인 목록 관련 API
 */

@Service
public class UpbitMyListService extends CommonService {

	@Value("#{systemProp['upbit.ackey']}")
	private String ackey;

	@Value("#{systemProp['upbit.sekey']}")
	private String sekey;

	public String getAckey() { return ackey; }

	public String getSekey() {
		return sekey;
	}



	@Autowired
	private UpbitAPIService upbitApiService;

	@Autowired
	private UpbitAPIMapper mapper;

	// -n% 하락에 대응하는 물타기 매수  (스케줄러로 작업하기) 30 / 1시간 기준으로 스케줄링돌리기
	public void insertCoinAutoBuySell(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		// 여기에 -4% 이상 떨어지는 코인 매수 리스트 짜고 따로 스케줄러 함수 하나 더만들기 ( 파라미터 넘겨야됨 )
		// 현재 해당함수 파라미터도 수정하기  ( 그냥 단순 KRW-MARKET, 하락률(%) 파라미터 이런것만 받기)


		// 현재 업비트 코인시세 리스트
		List<Map<String, Object>> quoteList = upbitApiService.selectQuoteCoinList();

		// 내가 구매한 코인 리스트 ( 자동매수 = 'Y' 인 코인들만 )
		List<Map<String, Object>> myList = mapper.selectDBCoinMyList();

		// 코인전체시세 리스트
		for(int i=0; i<quoteList.size(); i++) {

			// 나의 매수한 전체코인 리스트
			for(int k=0; k<myList.size(); k++) {

				// 전체 코인리스트에서 내가 매수한 코인명과 같다면..
				if(quoteList.get(i).get("MARKET").equals(myList.get(k).get("MARKET"))) {

					String market = myList.get(k).get("MARKET").toString();
					Double quoteTRADE_PRICE = Double.parseDouble(quoteList.get(i).get("TRADE_PRICE").toString());	// 현재 코인시세
					Double myAVG_BUY_PRICE = Double.parseDouble(myList.get(k).get("AVG_BUY_PRICE").toString());		// 내가 매수한 평단
					String balance = myList.get(k).get("BALANCE").toString();	// 내가 매수한 코인 수량
					String balance_price = myList.get(k).get("BALANCE_PRICE").toString();			// 내가 매수한 원화금액(KRW)

					Double result = ((quoteTRADE_PRICE - myAVG_BUY_PRICE) / myAVG_BUY_PRICE) * 100;	// (상승/하락) 률(%)
					Double absResult = Math.abs(result);	// 몇이상에 매수or매도를 진행할것인지
					long profitMoney = Math.round((Double.parseDouble(balance_price) * result) / 100);	// 실제 수익금액 / 손실금액

					System.out.println("--jytest--");
					System.out.println("market : " + market);
					System.out.println("blanace_price : " + balance_price);
					System.out.println("result : " + result);
					System.out.println(profitMoney);
					System.out.println("--jytest--");

					// 상승인 경우 (매도)
					if(result > 0) {

						// 매도진행	( 숫자도 DB에서 가져온 값으로 대체하기 DB타입:FLOAT)
						if(absResult >= 10) {

							//selectCoinBuySell(market, balance_price, balance, "SELL"); // 매도
							// 매도를 했을시 MY_LIST 테이블에도 매도를 한다
							Map<String, Object> delMap = new HashMap<>();
							delMap.put("MARKET", market);
							mapper.deleteReloadMyList(delMap);

							// T_COIN_TRADE_HIS 테이블에 insert하기
							// profitMoney 값을 TRADE_HIS 테이블에 실제 수익금으로 넣은후
							// 해당수익금이 SUM() 해서 10만원 이상 될경우 출금


						}

					// 하락인 경우 (매수)
					} else {

						// 매수진행	( 숫자도 DB에서 가져온 값으로 대체하기 DB타입:FLOAT)
						if(absResult >= 2.5) {

							// selectCoinBuySell(market, balance_price, "", "BUY"); //매수
							// T_COIN_TRADE_HIS 테이블에 insert하기 (매수때 꼭 필요한가 한번 생각해보기 매수는 필요없을거 같음)
						}
					}
				}
			}
		}
		logger.info("T_COIN_MY_LIST.TRADE_USE_YN = 'Y'인 코인 개수 : " + myList.size());
	}

	/**
	 * 업비트에서 매수한 코인 전체 리스트를 DB 저장  (7분마다 스케줄러 실행)
	 * @throws Exception
	 */
	@Scheduled(cron = "0 7 0 * * *")
	public void updateDBCoinMyList() throws Exception {

		// 업비트에서 내가 매수한 코인 전체 리스트를 가져온다
		List<Map<String, Object>> myList = selectUpbitCoinMyList();

		// 매수한 전체 리스트를 DB에 MERGE INTO
		mapper.updateDBCoinMyList(myList);
	}


	/* ****************************** 업비트 API에서 가져오는 함수 Start ****************************** */
	/**
	 * 업비트에서 매수한 코인 전체 리스트 가져오기
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectUpbitCoinMyList() throws Exception {

		// API를 통해 코인 데이터를 가져올떄마다 API_MY_LIST 테이블을 초기화 한다 (API_MY_LIST 테이블은 TEMP 역활이라 보면됨)
		mapper.deleteAPICoinMyList();

		// API에서 날라온 값 그대로 저장할 리스트
		List<Map<String, Object>> apiListMap = new ArrayList<>();

		// 업비트 API에서 실제 내가 매수한 코인을 담기위한 리스트
		List<Map<String, Object>> listMap = new ArrayList<>();

		// DB에서 코인리스트API 새로 호출하기전까지 저장되어있는 코인 목록
		List<Map<String, Object>> myList = mapper.selectDBCoinMyList();

		String accessKey = getAckey();
		String secretKey = getSekey();
		String serverUrl = "https://api.upbit.com";

		// jwt토근 암호화
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		String jwtToken = JWT.create()
				.withClaim("access_key", accessKey)
				.withClaim("nonce", UUID.randomUUID().toString())
				.sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
			request.setHeader("Content-Type", "application/json");
			request.addHeader("Authorization", authenticationToken);

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "UTF-8");	// HttpEntity (response값을 String으로 형변환)


			// 현재 날짜 구하기
			LocalDate now = LocalDate.now();
			// 포맷 정의
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			// 포맷 적용
			String formatedNow = now.format(formatter);

			// ---------------- Json Array 파싱법 (배열key 값이 없는 일반 배열로 감싸져있는 json배열 파싱) ----------------
			JsonParser parser = new JsonParser();
			JsonArray jsonArray = (JsonArray) parser.parse(result);

			for(int i=0; i<jsonArray.size(); i++) {

				JsonObject allCoin = (JsonObject) jsonArray.get(i);
				Map<String, Object> map = new LinkedHashMap<>();

				// 아래방식으로 전체 리스트를 뽑아온다음 list를 return 해서 다른 함수에서 값 비교 및 매수진행하기
				// 전체 리스트를 보려면 이렇게 하면됨
				map.put("MARKET", "KRW-"+allCoin.get("currency").getAsString());
				map.put("BALANCE", allCoin.get("balance").getAsString());
				map.put("AVG_BUY_PRICE", allCoin.get("avg_buy_price").getAsString());
				// 수량 * 코인평단가 = 원화(KRW) 매수금액
				double balance = Double.parseDouble(allCoin.get("balance").getAsString());
				double avgBuyPrice = Double.parseDouble(allCoin.get("avg_buy_price").getAsString());
				double balancePrice = Math.round(balance * avgBuyPrice);
				map.put("BALANCE_PRICE", balancePrice);	// 매수금액 (원화)

				map.put("LOCKED", allCoin.get("locked").getAsString());
				map.put("AVG_BUY_PRICE_MODIFIED", allCoin.get("avg_buy_price_modified").getAsString());
				map.put("UNIT_CURRENCY", allCoin.get("unit_currency").getAsString());
				map.put("FR_TRADE_DATE", formatedNow);
				//map.put("UPD_DT", upd_dt);	// upd_dt 자바 포맷팅 활용하거나 DB 데이터 타입을 VARCHAR로 변경후 기입하기


				// 업비트 투자내역에서 쓰래기 데이터의 토큰(코인)들은 제외한다 ( 비토르토큰, 에이피이엔에프터 )
				if(!map.get("MARKET").equals("KRW-VTHO") && !map.get("MARKET").equals("KRW-APENFT")) {
					apiListMap.add(map);
					listMap.add(map);
				}

			}

			// 업비트 API 매수 리스트와 나의 DB 코인 매수 리스트 동기화 *************************
			/*
				API_MY_LIST 테이블에 현재 업비트에 있는 MARKET을 insert하고
				현재 DB에 있는 MY_LIST 테이블에서 (모바일) 매도한 코인은 API 테이블과 비교하여 API테이블에 맞게 동기화 처리
			*/
			mapper.insertApiCoinList(apiListMap);
			updateSyncApiDb();	// 나의 코인리스트 동기화 (Upbit <-> DB)
			// ******************************************************************************

		} catch (IOException e) {
			e.printStackTrace();
		}

		return listMap;
	}



	/**
	 * 코인 매수/매도
	 * @param market		- market명
	 * @param balance_price - 금액 (원화KRW)
	 * @param balance		- 물량 ( 매도시 사용하는 파라미터, 매수에는 사용안함)
	 * @param trade			- BUY = 매수  /  SELL = 매도
	 * @throws Exception
	 */
	public void selectCoinBuySell(String market, String balance_price, String balance, String trade) throws Exception {
		// 두 암호키는 프로퍼티값 가져와서 테스트하기 ( 현재 main에서는 테스트 안됨 )
		String accessKey = getAckey();
		String secretKey = getSekey();
		String serverUrl = "https://api.upbit.com";

		HashMap<String, String> params = new HashMap<>();

		params.put("market", market);

		// ---------------- 매수 ----------------
		if(trade.equals("BUY")) {
			params.put("side", "bid");			// *매수
			params.put("price", balance_price);		// *매수시 (KRW)가격
			params.put("ord_type", "price");	// *시장가 매수
			//params.put("ord_type", "limit");	// *지정가 매수

		// ---------------- 매도 ----------------
		} else if(trade.equals("SELL")) {
			params.put("side", "ask");			// 매도
			params.put("volume", balance);		// 매도시 수량
			params.put("ord_type", "market");	// 시장가 매도
		}


		ArrayList<String> queryElements = new ArrayList<>();
		for(Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		String jwtToken = JWT.create()
				.withClaim("access_key", accessKey)
				.withClaim("nonce", UUID.randomUUID().toString())
				.withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512")
				.sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost request2 = new HttpPost(serverUrl + "/v1/orders");
			request2.setHeader("Content-Type", "application/json");
			request2.addHeader("Authorization", authenticationToken);
			request2.setEntity(new StringEntity(new Gson().toJson(params)));

			HttpResponse response2 = client.execute(request2);
			HttpEntity entity = response2.getEntity();

			System.out.println(EntityUtils.toString(entity, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/* ****************************** 업비트 API에서 가져오는 함수 End ****************************** */

	// 나의코인리스트 업비트 <-> DB 동기화
	public void updateSyncApiDb() throws Exception{

		// MY_LIST <-> API_MY_LIST 테이블 서로 동기화 (API 테이블에 없는코인 찾기)
		List<Map<String, Object>> chkList = mapper.selectChkAPiDbMyList();

		for (int i=0; i<chkList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("MARKET", chkList.get(i).get("MARKET"));

			// API테이블에 없는 코인이 있다면 MY_LIST테이블에 해당 코인을 삭제한다
			mapper.deleteReloadMyList(map);
		}
	}

}

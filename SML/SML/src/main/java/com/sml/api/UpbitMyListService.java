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
	public void insertBuyDownCoin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		// 여기에 -4% 이상 떨어지는 코인 매수 리스트 짜고 따로 스케줄러 함수 하나 더만들기 ( 파라미터 넘겨야됨 )
		// 현재 해당함수 파라미터도 수정하기  ( 그냥 단순 KRW-MARKET, 하락률(%) 파라미터 이런것만 받기)


		// 현재 업비트 코인시세 리스트
		List<Map<String, Object>> quoteList = upbitApiService.selectQuoteCoinList();

		// 내가 구매한 코인 리스트
		List<Map<String, Object>> myList = selectUpbitCoinMyList();	// ★★ DB에서 값 뽑아오기 (WHERE TRADE_USE_YN = 'Y'
		//List<Map<String, Object>> myList = mapper.selectDBCoinMyList();	// 위에 주석 지우고 해당 주석 해제후 DB에서 값 가져오기

		System.out.println("--=jytest--");

		// 코인전체시세 리스트
		for(int i=0; i<quoteList.size(); i++) {

			// 나의 매수한 전체코인 리스트
			for(int k=0; k<myList.size(); k++) {

			}
			if(quoteList.get(i).get("MARKET").equals("KRW-BTC")) {
				System.out.println(quoteList.get(i));
			}
		}
		System.out.println("--=jytest--");

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
		List<Map<String, Object>> listMap = new ArrayList<>();

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
				map.put("BALANCE_PRICE", balancePrice);

				map.put("LOCKED", allCoin.get("locked").getAsString());
				map.put("AVG_BUY_PRICE_MODIFIED", allCoin.get("avg_buy_price_modified").getAsString());
				map.put("UNIT_CURRENCY", allCoin.get("unit_currency").getAsString());
				map.put("FR_TRADE_DATE", formatedNow);

				listMap.add(map);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return listMap;
	}

	/* 코인 매수/매도 */
	public void selectCoinBuySell() throws Exception {
		// 두 암호키는 프로퍼티값 가져와서 테스트하기 ( 현재 main에서는 테스트 안됨 )
		String accessKey = getAckey();
		String secretKey = getSekey();
		String serverUrl = "https://api.upbit.com";

		HashMap<String, String> params = new HashMap<>();
		params.put("market", "KRW-MVL");

		// ---------------- 매수 ----------------
//////		params.put("side", "bid");			// *매수
//////		params.put("price", "5500");		// *매수시 (KRW)가격
//////		params.put("ord_type", "price");	// *시장가 매수
//////		params.put("ord_type", "limit");	// *지정가 매수

		// ---------------- 매도 ----------------
//		params.put("side", "ask");			// 매도
//		params.put("volume", "1565");		// 매도시 수량
//		params.put("ord_type", "market");	// 시장가 매도

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

}

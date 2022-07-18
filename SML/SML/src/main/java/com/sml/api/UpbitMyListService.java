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
	private UpbitAPIMapper mapper;


	public void updateCoinMyList(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Map<String, Object>> list = selectCoinMyList();
		mapper.updateCoinMyList(list);
	}

	/* 매수한 코인 전체 리스트 */
	public List<Map<String, Object>> selectCoinMyList() throws Exception {
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


}

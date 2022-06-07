package com.sml.test;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sml.utils.util.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Test {

    public static void main(String args[]) throws Exception {


        // 1. coin리스트를 List<Map>에 담는다
        List<Map<String, Object>> coinList = getUpbitCoinList("KRW");

        // 리스트맵에서 market키값을 가져온다
        String str = parsingJsonToString(coinList, "market");

        // 마지막 쉼표제거를 위해 replace
        String strReplace = StringUtil.lastStringDelete(str);

        // 코인가격이 배열로 JSON형태의 값을 가지고 온다
        String coinQuoteList = getCoinQuoteList(strReplace);

        // 코인시세(quote) 파싱작업
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(coinQuoteList);
        JSONArray jsonArr = (JSONArray)obj;

        for(int i=0; i<5; i++) {
            JSONObject jsonObj = (JSONObject)jsonArr.get(i);
            System.out.println( (String)jsonObj.get("market") );      // 코인 이름
            System.out.println( (double)jsonObj.get("high_price") );  // 오늘 고가
        }

    }




    // UpBit) KRW / BTC 코인명 전체 리스트 가져오기
    public static List<Map<String, Object>> getUpbitCoinList(String coinCode) throws IOException {

        List<Map<String, Object>> listKRWMap = new ArrayList<>();
        List<Map<String, Object>> listBTCMap = new ArrayList<>();

        HttpsURLConnection conn = null;
        BufferedReader rd = null;

        try {
            int CONNECT_TIMEOUT_VALUE = 5000; // URL 연결 실패 시 시간설정(5초)
            int READ_TIMEOUT_VALUE = 5000;    // 데이터 조회 시간설정(5초)

            StringBuilder urlBuilder = new StringBuilder("https://api.upbit.com/v1/market/all?isDetails=false"); //URL
            //StringBuilder urlBuilder = new StringBuilder("https://api.upbit.com/v1/ticker/markets="+KRW-BTC); //URL
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
                        krwMap.put("market", market);
                        krwMap.put("korean_name", korean_name);
                        krwMap.put("english_name", english_name);

                        listKRWMap.add(krwMap);
                    }

                // 파라미터가 BTC일경우 BTC마켓에 코인리스트만 들고온다
                } else if(coinCode.equals("BTC")) {

                    if(market.substring(0, 3).equals("BTC")) {
                        btcMap.put("market", market);
                        btcMap.put("korean_name", korean_name);
                        btcMap.put("english_name", english_name);

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

    // List<Map>에 특정값만 뽑아온다
    public static String parsingJsonToString(List<Map<String, Object>> listMap, String val) {

        String str = "";
        for (int i = 0; i < listMap.size(); i++) {
            str += listMap.get(i).get(val) + ",";
        }
        return str;
    }

    /**
     * 코인시세정보 가져오기
     * @param str
     * @return
     */
    public static String getCoinQuoteList(String str) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("https://api.upbit.com/v1/ticker?markets="+str, HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();

        System.out.println("Response status: " + status);
        System.out.println(response);

        return response;
    }
}

package com;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Test {

    public static void main(String args[]) throws IOException {

        String a = "CMN01.053-632-6338";
        String b = a.substring(a.indexOf(".") + 1);
        System.out.println(b);


        String LoginID = "itc"; // 테스트용 ( SessionUtil.get(""); 아이디값 가져올수있으면 다음엔 그렇게 하기
        String interfaceID = "123";
        String interfaceDT = "456";

        HttpsURLConnection conn = null;
        BufferedReader rd = null;

        try {
            int CONNECT_TIMEOUT_VALUE = 5000; // URL 연결 실패 시 시간설정(5초)
            int READ_TIMEOUT_VALUE = 5000;    // 데이터 조회 시간설정(5초)

            StringBuilder urlBuilder = new StringBuilder("https://api.upbit.com/v1/ticker?markets=KRW-BTC"); //URL
//            urlBuilder.append("?" + URLEncoder.encode("LoginID", "UTF-8") + "=" + URLEncoder.encode(LoginID, "UTF-8"));

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
            JsonObject object = (JsonObject) jsonArray.get(0);


            /*
            *   현재 result값은 [ {... } ] 형태로 배열로 넘어옴 첫번째 배열값에 키값이 없으면 위처럼 jsonArray.get(0); 으로 하면됨
            */

            // json 첫번째배열 전체값 가져오기
            System.out.println("\n■■■■■■■■■■■■■■■■■■■■■■■ json array ■■■■■■■■■■■■■■■■■■■■■■■");
            System.out.println(jsonArray);
            System.out.println("------json array----\n");

            // json 첫번재배열에 market Key값 가져오기
            System.out.println("\n■■■■■■■■■■■■■■■■■■■■■■■ json object ■■■■■■■■■■■■■■■■■■■■■■■");
            System.out.println("\"market\" : " + object.get("market").getAsString()); // getAsString() 을 할경우 value값 양쪽에 "" (더블쿼트) 제거
            System.out.println("■■■■■■■■■■■■■■■■■■■■■■■ json object ■■■■■■■■■■■■■■■■■■■■■■■\n");



        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if(rd != null){
                rd.close();
            }
            // 2019년 8월 SW취약성 점검결과 조치
            if(conn != null) {
                conn.disconnect();
            }
        }


    }
}

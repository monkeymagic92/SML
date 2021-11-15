package com;

import com.google.gson.JsonElement;
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

        String LoginID = "itc"; // 테스트용 ( SessionUtil.get(""); 아이디값 가져올수있으면 다음엔 그렇게 하기
        String interfaceID = "123";
        String interfaceDT = "456";

        HttpsURLConnection conn = null;
        BufferedReader rd = null;

        try {
            int CONNECT_TIMEOUT_VALUE = 5000; //미세먼지 URL 연결 실패 시 시간설정(5초)
            int READ_TIMEOUT_VALUE = 5000;    //미세먼지 데이터 조회 시간설정(5초).....

            StringBuilder urlBuilder = new StringBuilder("https://api.upbit.com/v1/market/all?"); //URL
//            urlBuilder.append("?" + URLEncoder.encode("LoginID", "UTF-8") + "=" + URLEncoder.encode(LoginID, "UTF-8"));

            URL url = new URL(urlBuilder.toString());

            conn = (HttpsURLConnection) url.openConnection();

            conn.setConnectTimeout(CONNECT_TIMEOUT_VALUE);	//서버 접속시 연결 시간
            conn.setReadTimeout(READ_TIMEOUT_VALUE);		//연결 후 데이터 가져오는 시간
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json;utf-8");

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

            System.out.println("--jytest--");
            System.out.println(sb);
            System.out.println("--jytest--");

            if(sb.length() > 0) {
                result += line;
            }

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

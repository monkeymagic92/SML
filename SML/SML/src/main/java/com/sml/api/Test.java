package com.sml.api;

import javax.lang.model.util.Elements;
import javax.swing.text.Document;

/**
 * 프로그램명  : Test
 * 날짜       : 2022-05-24 / 화요일 / 오후 11:36
 * 설명       :
 */
public class Test {

	public static void main(String args[]) {
		String url = "https://sigbtc.pro/derivatives";
		String agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";

		/*
			div class="mt-2 font-weight-500
			span class="btcKimp text-up"
		*/
		/*
			Jsoup 라이브러리 다운받기
		 */

//		Document doc = Jsoup.connect(url).userAgent(agent).get();
//		Elements contents = doc.select(".xrpKimp text-up");

//		System.out.println(doc);
//		System.out.println("--jytest--");
//		System.out.println(contents);
//		System.out.println("--jytest--");
	}

	public static String test(String str) {
		String temp = "";
		temp += str;
		return temp;
	}
}

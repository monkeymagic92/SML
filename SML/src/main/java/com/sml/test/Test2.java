package com.sml.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 프로그램명  : Test2
 * 날짜       : 2022-03-18 / 금요일 / 오후 11:14
 * 설명       :
 */
public class Test2 {

	public static void main(String[] args) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("market", "KRW-BTC");
		map.put("quote", 123123);

		listMap.add(map);

		map.put("market", "KRW-ETH");
		map.put("quote", 00000);

		listMap.add(map);

		for(Map<String, Object> strMap : listMap){
			System.out.println(strMap);
			System.out.println(listMap.size());
		}

	}
}

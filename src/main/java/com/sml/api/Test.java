package com.sml.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 프로그램명  : Test
 * 날짜       : 2022-06-05 / 일요일 / 오후 1:16
 * 설명       :
 */
public class Test {

	public static void main(String[] args) {

		// 초성(자음) 검색기능, 첫번째 글자 추출하기

//		String str = "테스트";
//		String[] chs = { "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ"
//				, "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ" };
//		char ch = str.charAt(0);
//		int uniVal = ch - 0xAC00;
//		System.out.println(chs[(((uniVal - (uniVal % 28)) / 28) / 21)]);

		List<String> index_list = new ArrayList<>();

		index_list.add("ㄱ");    index_list.add("ㄴ");    index_list.add("ㄷ");
		index_list.add("ㄹ");    index_list.add("ㅁ");    index_list.add("ㅂ");
		index_list.add("ㅅ");    index_list.add("ㅇ");    index_list.add("ㅈ");
		index_list.add("ㅊ");    index_list.add("ㅋ");    index_list.add("ㅌ");
		index_list.add("ㅍ");    index_list.add("ㅎ");

		Map<Integer, String> index_map = new HashMap<>();

		index_map.put(0, "가");  index_map.put(1, "나");  index_map.put(2, "다");
		index_map.put(3, "라");  index_map.put(4, "마");  index_map.put(5, "바");
		index_map.put(6, "사");  index_map.put(7, "아");  index_map.put(8, "자");
		index_map.put(9, "차");  index_map.put(10, "카");  index_map.put(11, "타");
		index_map.put(12, "파");  index_map.put(13, "하"); index_map.put(14, "힣");

		String index = "몰라";
		int num = 0;
		String whereSQL = "";

		for( int i = 0; i < index_list.size(); i++ ) {
			if( index.equals(index_list.get(i)) ) {
				num = i;
				break;
			}
		}

		System.out.println(index_map.get(num));
		System.out.println(index_map.get(num+1));

	}
}

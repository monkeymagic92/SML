package com.sml.api;

/**
 * 프로그램명  : Test
 * 날짜       : 2022-05-24 / 화요일 / 오후 11:36
 * 설명       :
 */
public class Test {

	public static void main(String args[]) {

		String arr = "";
		String result = "";
		for(int i=0; i<10; i++) {
			arr += "" + i;
			result = test(arr);
		}

		System.out.println(result);
	}

	public static String test(String str) {
		String temp = "";
		temp += str;
		return temp;
	}
}

package com.sml.test;

import com.sml.utils.util.TimeMaximum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 프로그램명  : SampleServiceImpl
 * 날짜       : 2022-02-05 / 토요일 / 오후 6:52
 * 설명       :
 */

@Service
public class SampleTest {

	// resources - config - config-properties 파일안에 있는 프로퍼티값을 자바에서 사용하기위함 (설정: context-commmon.xml - <util:properties ..>)
	@Value("#{systemProp['db.maria.username']}")
	private String userName;


	/*
		스케줄러 ( 10초에 한번씩 자동실행, (1000ms)
	 */
//	@Scheduled(fixedRate=10000)
	public void sec(HttpServletRequest request) {

		// 인터셉터에서 값 가져오기
		String interceptorTest = (String) request.getAttribute("interceptorTest");
		System.out.println(interceptorTest);
		////////////////////////////////////////////////



		/*------ 시,일,월 얼마나 경과했는지 알려주는 로직 ------*/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = sdf.parse("2021-11-08 12:33:33"); // 예시1
			// date = sdf.parse("2021-11-05 12:34:56");  예시2
			//date = sdf.parse("2020-05-08 12:11:24");  예시3

		} catch (Exception e) {
			e.printStackTrace();
		}

		String strR_dt = TimeMaximum.calculateTime(date);
		System.out.println("경과시간 : " + strR_dt);
		////////////////////////////////////////////////



		// 스케줄러
		System.out.println("--------------------------------------jytest");
		System.out.println("스케줄러임");
		System.out.println("--------------------------------------jytest");
		////////////////////////////////////////////////
	}
}

package com.sml.utils.exception;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 프로그램명  : ExceptionAdvice
 * 날짜       : 2022-02-07 / 월요일 / 오후 9:51
 * 설명       :
 */
public class ExceptionAdvice {

	private static Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

	public Object aroundMethodCall(ProceedingJoinPoint jp) throws Throwable {

		Object obj = null;
		try {
			obj = jp.proceed();

		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.info("■■■■■■■■■■■■■ ExceptionAdvice After Method");
		return obj;
	}
}

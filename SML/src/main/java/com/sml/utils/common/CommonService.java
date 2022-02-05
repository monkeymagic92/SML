package com.sml.utils.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

/**
 * 프로그램명  : CommonService
 * 날짜       : 2022-01-27 / 목요일 / 오후 8:56
 * 설명       :
 */
public abstract class CommonService {

	// CommonService 생성자
	public CommonService() {}

	// Log
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	protected Properties systemProp;

}

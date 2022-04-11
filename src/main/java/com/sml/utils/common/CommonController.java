package com.sml.utils.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class CommonController {

    // ComController 생성자
    public CommonController() {}

    // Log
    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    protected Properties systemProp;	// 상속받을 자식 service에 CommonService 상속해주기

}

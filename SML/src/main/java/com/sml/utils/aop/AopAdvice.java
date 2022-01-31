package com.sml.utils.aop;

import com.sml.utils.common.CommonController;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AopAdvice extends CommonController {

    public void beforeMethodCall(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        Date time = new Date();

        String CRT_DT = format1.format(time);
        String UPD_DT = format1.format(time);

        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("CRT_DT", CRT_DT);
        baseMap.put("UPD_DT", UPD_DT);

        request.setAttribute("baseMap", baseMap);
        request.setAttribute("aop", "AOP에서 시작되었다 (AopAdvice - beforeMethodCall)");
    }
}

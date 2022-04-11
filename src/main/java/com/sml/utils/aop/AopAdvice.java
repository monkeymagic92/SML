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

    public void beforeMethodCall(JoinPoint jp) throws Exception {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // CRT_DT, UPD_DT
        setBaseInfoColumn(request);

        // img, js, css
        setResourcesFileName(request);

        request.setAttribute("aop", "AOP에서 시작되었다 (AopAdvice - beforeMethodCall)");
    }

    /**
     * 기본정보 셋팅
     * @param request
     * @throws Exception
     */
    private void setBaseInfoColumn(HttpServletRequest request) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        String dt = sdf.format(new Date()); //일시
        String ipAddr = request.getRemoteAddr(); //IP주소

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CRT_DT", dt);          //생성일시
        map.put("CRT_IP_ADDR", ipAddr); //생성IP주소
        map.put("UPD_DT", dt);          //생성일시
        map.put("UPD_IP_ADDR", ipAddr); //생성IP주소

        Map<String, Object> mapu = new HashMap<String, Object>();
        mapu.put("UPD_DT", dt);          //생성일시
        mapu.put("UPD_IP_ADDR", ipAddr); //생성IP주소

        request.setAttribute("baseInfoColumn", map);
        request.setAttribute("baseInfoColumnU", mapu);

    }

    private void setResourcesFileName(HttpServletRequest request) throws Exception {
        String imgAddr = "/res/img";
        String jsAddr = "/res/js";
        String cssAddr = "/res/css";

        request.setAttribute("img", imgAddr);
        request.setAttribute("js", jsAddr);
        request.setAttribute("css", cssAddr);
    }
}

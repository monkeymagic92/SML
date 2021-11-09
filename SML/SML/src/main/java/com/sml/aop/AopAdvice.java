package com.sml.aop;

import com.sml.common.ComController;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AopAdvice extends ComController {

    public void beforeMethodCall(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        request.setAttribute("aop", "AOP에서 시작되었다 (AopAdvice - beforeMethodCall)");
    }
}

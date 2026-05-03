package com.clougence.clouddm.console.web.global.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeCostInterceptor implements HandlerInterceptor {

    //before the actual handler will be executed
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("req_start_time", startTime);
        MDC.put("req_start_time", String.valueOf(startTime));
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception arg3) {
        long startTime = (Long) request.getAttribute("req_start_time");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        if (executeTime > 1500) {
            log.info("request execution time is too long of '" + request.getRequestURI() + "' cost time:" + executeTime + ", start time:" + startTime);
        }

        MDC.remove("req_start_time");
    }

}

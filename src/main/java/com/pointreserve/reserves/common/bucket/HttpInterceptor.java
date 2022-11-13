package com.pointreserve.reserves.common.bucket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
//@Slf4j
//public class HttpInterceptor implements HandlerInterceptor {
//    private TrafficPlanService trafficPlanService = new TrafficPlanService();
//
//    private ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler
//    ) throws IOException {
//        Bucket bucket = trafficPlanService.resolveBucket(request);
//        if(bucket.tryConsume(1)) return true;
//
//        // TODO - 429를 보내고 싶은데, HttpServletResponse에서 429를 제공하지 않음. 원래 여기서 처리하는 것이 아닌지?
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//        response.getWriter().write(mapper.writeValueAsString("TOO MANY REQUEST"));
//        return false;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request,
//                                HttpServletResponse response,
//                                Object handler,
//                                Exception ex) {
//        log.info("================ Method Completed");
//        log.info(response.toString());
//    }
//
//    public void clearCache(){
//        trafficPlanService.clearCache();
//    }
//
//}

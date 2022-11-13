package com.pointreserve.reserves.common.bucket;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TrafficPlanService {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(HttpServletRequest request) {
        return cache.computeIfAbsent(getRemoteAddress(request), this::newBucket);
    }

    public void clearCache(){
        cache.clear();
    }

    private Bucket newBucket(String apiKey){
        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(3, Duration.ofSeconds(30))))
                .build();
    }

    private String getRemoteAddress(HttpServletRequest request){
        return request.getRemoteAddr();
    }
}

package com.pointreserve.reserves.accumulationpoint.ui.controller;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointCreate;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.common.bucket.TrafficPlanService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccumulatedPointController {

    private final TrafficPlanService trafficPlanService;

    private final AccumulatedPointService accumulatedPointService;

    @PostMapping("/reserves/create/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> createAccount(@PathVariable(name = "memberId") Long memberId, HttpServletRequest request){

        Bucket bucket = trafficPlanService.resolveBucket(request);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        HttpHeaders httpHeaders = new HttpHeaders();

        if(probe.isConsumed()){
            AccumulatedPointResponse response = accumulatedPointService.createAccumulatedPoint(AccumulatedPointCreate.builder()
                                                                        .memberId(memberId).build());
            httpHeaders.set("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens())); // 윈도 내 남은 처리 요청수
            httpHeaders.set("X-RateLimit-Limit", String.valueOf(bucket.getAvailableTokens())); // 매 윈도우 마다 클라이언트가 전송할 수 있는 요청의 수
            return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(response);
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        httpHeaders.set("X-RateLimit-Limit", String.valueOf(bucket.getAvailableTokens())); // 매 윈도우 마다 클라이언트가 전송할 수 있는 요청의 수
        httpHeaders.set("X-Ratelimit-Retry-After", String.valueOf(waitForRefill)); // 한도 제한에 걸리지 않으려면 몇 초 뒤에 요청을 다시 보내야 하는지?

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(httpHeaders).build();
    }

    @DeleteMapping("/reserves/delete/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> deleteAccount(@PathVariable Long memberId, HttpServletRequest request) {
        Bucket bucket = trafficPlanService.resolveBucket(request);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        HttpHeaders httpHeaders = new HttpHeaders();

        if(probe.isConsumed()) {
            httpHeaders.set("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens())); // 윈도 내 남은 처리 요청수
            httpHeaders.set("X-RateLimit-Limit", String.valueOf(bucket.getAvailableTokens())); // 매 윈도우 마다 클라이언트가 전송할 수 있는 요청의 수
            return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(accumulatedPointService.deleteAccumulatedPoint(memberId));
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        httpHeaders.set("X-RateLimit-Limit", String.valueOf(bucket.getAvailableTokens())); // 매 윈도우 마다 클라이언트가 전송할 수 있는 요청의 수
        httpHeaders.set("X-Ratelimit-Retry-After", String.valueOf(waitForRefill)); // 한도 제한에 걸리지 않으려면 몇 초 뒤에 요청을 다시 보내야 하는지?

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(httpHeaders).build();
    }

    @GetMapping("/reserves/get/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> getAccount(@PathVariable Long memberId, HttpServletRequest request) {
        Bucket bucket = trafficPlanService.resolveBucket(request);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        HttpHeaders httpHeaders = new HttpHeaders();

        if(probe.isConsumed()){
            httpHeaders.set("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
            httpHeaders.set("X-RateLimit-Limit", String.valueOf(bucket.getAvailableTokens()));
            return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(accumulatedPointService.getAccumulatedPoint(memberId));
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        httpHeaders.set("X-RateLimit-Limit", String.valueOf(bucket.getAvailableTokens()));
        httpHeaders.set("X-Ratelimit-Retry-After", String.valueOf(waitForRefill));

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(httpHeaders).build();
    }

}

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

        if(probe.isConsumed()){
            AccumulatedPointResponse response = accumulatedPointService.createAccumulatedPoint(AccumulatedPointCreate.builder()
                                                                        .memberId(memberId).build());
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .headers(makeHeaders(bucket,probe))
                                 .body(response);
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                             .headers(makeHeaders(bucket,probe))
                             .build();
    }

    @DeleteMapping("/reserves/delete/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> deleteAccount(@PathVariable Long memberId, HttpServletRequest request) {
        Bucket bucket = trafficPlanService.resolveBucket(request);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if(probe.isConsumed()) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .headers(makeHeaders(bucket,probe))
                                 .body(accumulatedPointService.deleteAccumulatedPoint(memberId));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                             .headers(makeHeaders(bucket,probe))
                             .build();
    }

    @GetMapping("/reserves/get/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> getAccount(@PathVariable Long memberId, HttpServletRequest request) {
        Bucket bucket = trafficPlanService.resolveBucket(request);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if(probe.isConsumed()){
            return ResponseEntity.status(HttpStatus.OK)
                                 .headers(makeHeaders(bucket,probe))
                                 .body( accumulatedPointService.getAccumulatedPoint(memberId) );
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                             .headers(makeHeaders(bucket,probe))
                             .build();
    }

    private HttpHeaders makeHeaders(Bucket bucket, ConsumptionProbe probe) {
        HttpHeaders httpHeaders = new HttpHeaders();
        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        httpHeaders.set("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
        httpHeaders.set("X-RateLimit-Limit", String.valueOf(bucket.getAvailableTokens()));
        httpHeaders.set("X-Ratelimit-Retry-After", String.valueOf(waitForRefill));
        return httpHeaders;
    }

}

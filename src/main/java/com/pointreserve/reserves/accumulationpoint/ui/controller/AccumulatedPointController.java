package com.pointreserve.reserves.accumulationpoint.ui.controller;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointCreate;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccumulatedPointController {


    private final AccumulatedPointService accumulatedPointService;

    @PostMapping("/reserves/create/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> createAccount(@PathVariable(name = "memberId") Long memberId, HttpServletRequest request){
        ConsumptionProbe probe = setProbe(request.getRemoteAddr());

        probe.getRemainingTokens();

        if (probe.isConsumed()) {
            AccumulatedPointResponse response = accumulatedPointService.createAccumulatedPoint(AccumulatedPointCreate.builder()
                    .memberId(memberId).build());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        log.info("TOO MANY REQUEST. Wait Time {} Second ", waitForRefill);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/reserves/delete/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> deleteAccount(@PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(accumulatedPointService.deleteAccumulatedPoint(memberId));
    }

    @GetMapping("/reserves/get/{memberId}")
    public ResponseEntity<AccumulatedPointResponse> getAccount(@PathVariable Long memberId, HttpServletRequest request) {

        ConsumptionProbe probe = setProbe(request.getRemoteAddr());

        probe.getRemainingTokens();

        if (probe.isConsumed()) {
            return ResponseEntity.status(HttpStatus.OK).body(accumulatedPointService.getAccumulatedPoint(memberId));
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

        log.info("TOO MANY REQUEST. Wait Time {} Second ", waitForRefill);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    private ConsumptionProbe setProbe(String key) {
        Bucket bucket = accumulatedPointService.resolveBucket(key);
        return bucket.tryConsumeAndReturnRemaining(1);
    }

}

package com.pointreserve.reserves.account.ui.controller;

import com.pointreserve.reserves.account.application.service.AccountService;
import com.pointreserve.reserves.account.ui.dto.AccountCreate;
import com.pointreserve.reserves.account.ui.dto.AccountResponse;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;

    @PostMapping("/reserves/create/{memberId}")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable(name = "memberId") Long memberId){

        Bucket bucket = accountService.resolveBucket("");
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        long saveToekn = probe.getRemainingTokens();

        if (probe.isConsumed()) {
            AccountResponse response = accountService.createAccount(AccountCreate.builder()
                    .memberId(memberId).build());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        log.info("TOO MANY REQUEST. Wait Time {} Second ", waitForRefill);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/reserves/delete/{memberId}")
    public void deleteAccount(@PathVariable Long memberId) {
        accountService.deleteAccount(memberId);
    }

    @GetMapping("/reserves/get/{memberId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long memberId) {

        Bucket bucket = accountService.resolveBucket("five");
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        long saveToekn = probe.getRemainingTokens();


        if (probe.isConsumed()) {
//            log.info("Success");
//            log.info("Available Toekn : {} ", saveToekn);
            return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(memberId));
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

        log.info("TOO MANY REQUEST. Wait Time {} Second ", waitForRefill);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}

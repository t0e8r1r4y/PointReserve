package com.pointreserve.reserves.account.ui.controller;

import com.pointreserve.reserves.account.application.service.AccountService;
import com.pointreserve.reserves.account.ui.dto.AccountCreate;
import com.pointreserve.reserves.account.ui.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/reserves/create/{memberId}")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable(name = "memberId") Long memberId){
        AccountResponse response = accountService.createAccount(AccountCreate.builder()
                .memberId(memberId).build());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/reserves/delete/{memberId}")
    public void deleteAccount(@PathVariable Long memberId) {
        accountService.deleteAccount(memberId);
    }

    @GetMapping("/reserves/get/{memberId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(memberId));
    }
}

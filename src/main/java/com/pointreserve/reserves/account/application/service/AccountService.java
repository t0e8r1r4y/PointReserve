package com.pointreserve.reserves.account.application.service;

import com.pointreserve.reserves.account.domain.Account;
import com.pointreserve.reserves.account.domain.AccountEditor;
import com.pointreserve.reserves.account.exception.AccountConfilct;
import com.pointreserve.reserves.account.infra.AccountRepository;
import com.pointreserve.reserves.account.exception.AccountNotFound;
import com.pointreserve.reserves.account.ui.dto.AccountCreate;
import com.pointreserve.reserves.account.ui.dto.AccountEdit;
import com.pointreserve.reserves.account.ui.dto.AccountResponse;
import com.pointreserve.reserves.common.bucket.TrafficPlan;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        TrafficPlan trafficPlan = TrafficPlan.resolvePlanFromApiKey(apiKey);
        return Bucket.builder()
                .addLimit(trafficPlan.getLimit())
                .build();
    }

    @Transactional
    public AccountResponse createAccount( AccountCreate accountCreate ) {
        if( accountRepository.getByMemberId(accountCreate.getMemberId()).isPresent() ){
            throw new AccountConfilct();
        }
        return AccountResponse.builder()
                .account( accountRepository.save( accountCreate.toEntity() )  )
                .build();
    }

    @Transactional
    public void deleteAccount( Long memberId ) {
        Account account = accountRepository.getByMemberId(memberId)
                .orElseThrow(() -> new AccountNotFound());

        accountRepository.delete(account);
        return;
    }

    @Transactional
    public AccountResponse updateAccount(Long memberId, AccountEdit accountEdit){
        Account account = accountRepository.getByMemberId(memberId)
                .orElseThrow(() -> new AccountNotFound());

        AccountEditor.AccountEditorBuilder amountEditorBuilder = account.toEditor();
        AccountEditor accountEditor = amountEditorBuilder.totalAmount(accountEdit.getTotalAmount()).build();

        account.edit(accountEditor);
        Account saveResult = accountRepository.saveAndFlush(account);

        return new AccountResponse(saveResult);
    }
    @Transactional
    public AccountResponse getAccount( Long memberId ){
        Account account = accountRepository.getByMemberId(memberId)
                .orElseThrow(() -> new AccountNotFound());
        return AccountResponse.builder().account(account).build();
    }



}

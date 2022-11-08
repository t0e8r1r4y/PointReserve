package com.pointreserve.reserves.account.application.service;

import com.pointreserve.reserves.account.application.service.AccountService;
import com.pointreserve.reserves.account.domain.Account;
import com.pointreserve.reserves.account.infra.AccountRepository;
import com.pointreserve.reserves.account.exception.AccountNotFound;
import com.pointreserve.reserves.account.ui.dto.AccountCreate;
import com.pointreserve.reserves.account.ui.dto.AccountEdit;
import com.pointreserve.reserves.account.ui.dto.AccountResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void clean() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("적립금 계정 생성 서비스 테스트")
    void createReservesAccount() {
        Long memberId = 1L;

        accountService.createAccount(AccountCreate.builder()
                .memberId(memberId)
                .build());

        Assertions.assertEquals(1, accountRepository.count());
        Account account = accountRepository.findAll().get(0);
        Assertions.assertEquals(account.getMemberId(), memberId);
        Assertions.assertEquals(account.getTotalAmount(), 0);
    }

    @Test
    @DisplayName("적립금 계정 삭제 서비스 테스트")
    void deleteAccountTest() {
        // given
        accountRepository.save(Account.builder().memberId(1L).totalAmount(1000).build());

        //when
        accountService.deleteAccount(1L);

        //then
        Assertions.assertEquals(0, accountRepository.count());
    }

    @Test
    @DisplayName("적립금 계정 삭제 실패 서비스 테스트")
    void deleteAccountFailTest() {
        //then
        Assertions.assertThrows( AccountNotFound.class, () -> {
            accountService.deleteAccount(1L);
        });
    }

    @Test
    @DisplayName("적립금 계정 업데이트 테스트")
    void updateAccount() {
        // given
        accountRepository.save(Account.builder().memberId(1L).totalAmount(1000).build());
        // when
        AccountResponse response =  accountService.updateAccount(1L, AccountEdit.builder().totalAmount(20000).build());
        // then
        Assertions.assertEquals(20000, response.getTotalAmount());
    }

    @Test
    @DisplayName("적립금 계정 업데이트 동시성 테스트")
    void updateAccountConcurrency() throws InterruptedException {
        // given
        accountRepository.saveAndFlush(Account.builder().memberId(1L).totalAmount(1000).build());
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
               try {
                   accountService.updateAccount(1L, AccountEdit.builder().totalAmount(1000 + (100*(finalI +1))).build());
               } finally {
                   latch.countDown();
               }
            });
        }

        latch.await();

        // then
        Optional<Account> response = accountRepository.getByMemberId(1L);
        System.out.println(response.get().getTotalAmount());
        Assertions.assertEquals(11000, response.get().getTotalAmount());
    }

    @Test
    @DisplayName("적립금 계정 업데이트 실패 테스트")
    void updateFailAccount() {
        // given
        accountRepository.save(Account.builder().memberId(1L).totalAmount(1000).build());

        //then
        Assertions.assertThrows( AccountNotFound.class, () -> {
            accountService.updateAccount(2L, AccountEdit.builder().totalAmount(20000).build());
        });
    }

    @Test
    @DisplayName("적립금 계정 정보 단건 조회 테스트")
    void getTest() {
        // given
        accountRepository.save(Account.builder().memberId(1L).totalAmount(1000).build());

        // when
        AccountResponse response = accountService.getAccount(1L);

        // then
        Assertions.assertEquals(response.getMemberId(), 1L);
        Assertions.assertEquals(response.getTotalAmount(), 1000);
    }

    @Test
    @DisplayName("적립금 계정 정보 단건 조회 실패 테스트")
    void getFailTest() {
        // given
        accountRepository.save(Account.builder().memberId(1L).totalAmount(1000).build());

        // when

        // then
        Assertions.assertThrows( AccountNotFound.class, () -> {
            accountService.getAccount(2L);
        });
    }
}

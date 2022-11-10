package com.pointreserve.reserves.accumulationpoint.application.service;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
import com.pointreserve.reserves.accumulationpoint.exception.AccountNotFound;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointCreate;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointEdit;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@ActiveProfiles("local")
class AccumulatedPointServiceTest {

    @Autowired
    private AccumulatedPointService accumulatedPointService;

    @Autowired
    private AccumulatedPointPointRepository accumulatedPointPointRepository;

    @BeforeEach
    void clean() {
        accumulatedPointPointRepository.deleteAll();
    }

    @Test
    @DisplayName("적립금 계정 생성 서비스 테스트")
    void createReservesAccount() {
        Long memberId = 1L;

        accumulatedPointService.createAccount(AccumulatedPointCreate.builder()
                .memberId(memberId)
                .build());

        Assertions.assertEquals(1, accumulatedPointPointRepository.count());
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.findAll().get(0);
        Assertions.assertEquals(accumulatedPoint.getMemberId(), memberId);
        Assertions.assertEquals(accumulatedPoint.getTotalAmount(), 0);
    }

    @Test
    @DisplayName("적립금 계정 삭제 서비스 테스트")
    void deleteAccountTest() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());

        //when
        accumulatedPointService.deleteAccount(1L);

        //then
        Assertions.assertEquals(0, accumulatedPointPointRepository.count());
    }

    @Test
    @DisplayName("적립금 계정 삭제 실패 서비스 테스트")
    void deleteAccountFailTest() {
        //then
        Assertions.assertThrows( AccountNotFound.class, () -> {
            accumulatedPointService.deleteAccount(1L);
        });
    }

    @Test
    @DisplayName("적립금 계정 업데이트 테스트")
    void updateAccount() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());
        // when
        AccumulatedPointResponse response =  accumulatedPointService.updateAccount(1L, AccumulatedPointEdit.builder().totalAmount(20000).build());
        // then
        Assertions.assertEquals(20000, response.getTotalAmount());
    }

    // 테스트가 틀렸다.
    // account update 관련 로직을 수정하고 테스트도 수정한다.
    @Test
    @DisplayName("적립금 계정 업데이트 동시성 테스트")
    void updateAccountConcurrency() throws InterruptedException {
        // given
        accumulatedPointPointRepository.saveAndFlush(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
               try {
                   accumulatedPointService.updateAccount(1L, AccumulatedPointEdit.builder().totalAmount(1000 + (100*(finalI +1))).build());
               } finally {
                   latch.countDown();
               }
            });
        }

        latch.await();

        // then
        Optional<AccumulatedPoint> response = accumulatedPointPointRepository.getByMemberId(1L);
        System.out.println(response.get().getTotalAmount());
        Assertions.assertEquals(11000, response.get().getTotalAmount());
    }

    @Test
    @DisplayName("적립금 계정 업데이트 실패 테스트")
    void updateFailAccount() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());

        //then
        Assertions.assertThrows( AccountNotFound.class, () -> {
            accumulatedPointService.updateAccount(2L, AccumulatedPointEdit.builder().totalAmount(20000).build());
        });
    }

    @Test
    @DisplayName("적립금 계정 정보 단건 조회 테스트")
    void getTest() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());

        // when
        AccumulatedPointResponse response = accumulatedPointService.getAccount(1L);

        // then
        Assertions.assertEquals(response.getMemberId(), 1L);
        Assertions.assertEquals(response.getTotalAmount(), 1000);
    }

    @Test
    @DisplayName("적립금 계정 정보 단건 조회 실패 테스트")
    void getFailTest() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());

        // when

        // then
        Assertions.assertThrows( AccountNotFound.class, () -> {
            accumulatedPointService.getAccount(2L);
        });
    }
}

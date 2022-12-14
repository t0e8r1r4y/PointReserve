package com.pointreserve.reserves.accumulationpoint.application.service;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointFactoryImpl;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointCreate;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.point.domain.PointStatus;
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

import static com.pointreserve.reserves.point.domain.PointStatus.REDEEM;
import static com.pointreserve.reserves.point.domain.PointStatus.SAVEUP;

@SpringBootTest
@ActiveProfiles("local")
class AccumulatedPointServiceTest {

    @Autowired
    private AccumulatedPointService accumulatedPointService;

    @Autowired
    private AccumulatedPointPointRepository accumulatedPointPointRepository;

    private AccumulatedPointFactoryImpl factory = new AccumulatedPointFactoryImpl();

    @BeforeEach
    void clean() {
        accumulatedPointPointRepository.deleteAll();
    }

    @Test
    @DisplayName("누적 포인트 계정 생성 테스트")
    void createReservesAccount() {
        Long memberId = 1L;

        accumulatedPointService.createAccumulatedPoint(AccumulatedPointCreate.builder()
                .memberId(memberId)
                .build());

        Assertions.assertEquals(1, accumulatedPointPointRepository.count());
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.findAll().get(0);
        Assertions.assertEquals(accumulatedPoint.getMemberId(), memberId);
        Assertions.assertEquals(accumulatedPoint.getTotalAmount(), 0);
    }

    @Test
    @DisplayName("누적 포인트 계정 삭제 테스트")
    void deleteAccountTest() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());

        //when
        accumulatedPointService.deleteAccumulatedPoint(1L);

        //then
        Assertions.assertEquals(0, accumulatedPointPointRepository.count());
    }

    @Test
    @DisplayName("누적 포인트 계정 삭제 실패 테스트")
    void deleteAccountFailTest() {
        //then
        Assertions.assertThrows( AccumulatedPointNotFoundException.class, () -> {
            accumulatedPointService.deleteAccumulatedPoint(1L);
        });
    }

    @Test
    @DisplayName("누적 포인트 계정 단건 조회 테스트")
    void getTest() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());

        // when
        AccumulatedPointResponse response = accumulatedPointService.getAccumulatedPoint(1L);

        // then
        Assertions.assertEquals(response.getMemberId(), 1L);
        Assertions.assertEquals(response.getTotalAmount(), 1000);
    }

    @Test
    @DisplayName("누적 포인트 계정 단건 조회 실패 테스트")
    void getFailTest() {
        // given
        accumulatedPointPointRepository.save(AccumulatedPoint.builder().memberId(1L).totalAmount(1000).build());

        // when

        // then
        Assertions.assertThrows( AccumulatedPointNotFoundException.class, () -> {
            accumulatedPointService.getAccumulatedPoint(2L);
        });
    }

    @Test
    @DisplayName("누적 포인트 적립 테스트 - 총 금액 = 기존 금액 + 적립금액")
    void calcPointAndUpdate() {
        // given
        Long givenId = 1L;
        int amount = 100;
        PointStatus s = SAVEUP;
        AccumulatedPoint given = factory.createAccumulatedPoint(givenId, 500);
        accumulatedPointPointRepository.save(given);

        // when
        AccumulatedPointResponse result = accumulatedPointService.calcPointAndUpdate(given.getMemberId(), amount,s);

        // then
        Assertions.assertEquals(result.getMemberId(), given.getMemberId());
        Assertions.assertEquals(result.getTotalAmount(), given.getTotalAmount()+amount);
    }

    @Test
    @DisplayName("누적 포인트 사용 테스트 - 총 금액 = 기존 금액 + 사용금액")
    void calcPointAndUpdate2() {
        // given
        Long givenId = 1L;
        int amount = 100;
        PointStatus s = REDEEM;
        AccumulatedPoint given = factory.createAccumulatedPoint(givenId, 500);
        accumulatedPointPointRepository.save(given);

        // when
        AccumulatedPointResponse result = accumulatedPointService.calcPointAndUpdate(given.getMemberId(), amount,s);

        // then
        Assertions.assertEquals(result.getMemberId(), given.getMemberId());
        Assertions.assertEquals(result.getTotalAmount(), given.getTotalAmount()-amount);
    }

    @Test
    @DisplayName("누적 포인트 적립 테스트, 적립 대상 계정이 없어서 실패")
    void calcPointAndUpdateFail() {
        // given
        Long givenId = 1L;
        int amount = 100;
        PointStatus s = SAVEUP;
        AccumulatedPoint given = factory.createAccumulatedPoint(givenId, 500);
        accumulatedPointPointRepository.save(given);

        // when

        // then
        Assertions.assertThrows( AccumulatedPointNotFoundException.class, () -> {
            accumulatedPointService.calcPointAndUpdate(2L, amount, s);
        });
    }

    @Test
    @DisplayName("누적 포인트 사용 테스트, 포인트 금액이 유효하지 않은 경우 실패")
    void calcPointAndUpdateFail2() {
        // given
        Long givenId = 1L;
        int amount = 10;
        PointStatus s = REDEEM;
        AccumulatedPoint given = factory.createAccumulatedPoint(givenId, 0);
        accumulatedPointPointRepository.save(given);

        // when

        // then
        Assertions.assertThrows( AccumulatedPointInvalidRequestException.class, () -> {
            accumulatedPointService.calcPointAndUpdate(1L, amount, s);
        });
    }

    @Test
    @DisplayName("누적 포인트 업데이트 테스트")
    void updateAccumulatedPoint() {
        // given
        Long givenId = 1L;
        int amount = 100;
        AccumulatedPoint given = factory.createAccumulatedPoint(givenId, 500);
        accumulatedPointPointRepository.save(given);
        given.edit(factory.createAccumulatedPointEditor(800));

        // when
        AccumulatedPointResponse result = accumulatedPointService.updateAccumulatedPoint(given);

        // then
        Assertions.assertEquals(result.getMemberId(), given.getMemberId());
        Assertions.assertEquals(result.getTotalAmount(), 800);
    }

    @Test
    @DisplayName("누적 포인트 적립 동시성 테스트")
    void updateAccountConcurrency() throws InterruptedException {
        // given
        accumulatedPointPointRepository.saveAndFlush(factory.createAccumulatedPoint(1L, 1000));
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    accumulatedPointService.calcPointAndUpdate(1L, 10, SAVEUP);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Optional<AccumulatedPoint> response = accumulatedPointPointRepository.getByMemberId(1L);
        System.out.println(response.get().getTotalAmount());
        Assertions.assertEquals(2000, response.get().getTotalAmount());
    }


    @Test
    @DisplayName("누적 포인트 사용 동시성 테스트")
    void updateAccountConcurrency2() throws InterruptedException {
        // given
        accumulatedPointPointRepository.saveAndFlush(factory.createAccumulatedPoint(1L, 1000));
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    accumulatedPointService.calcPointAndUpdate(1L, 10, REDEEM);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Optional<AccumulatedPoint> response = accumulatedPointPointRepository.getByMemberId(1L);
        System.out.println(response.get().getTotalAmount());
        Assertions.assertEquals(0, response.get().getTotalAmount());
    }

}

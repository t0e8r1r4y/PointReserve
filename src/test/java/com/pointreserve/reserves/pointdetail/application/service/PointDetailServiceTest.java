package com.pointreserve.reserves.pointdetail.application.service;

import com.pointreserve.reserves.pointdetail.domain.PointDetail;
import com.pointreserve.reserves.pointdetail.infra.PointDetailRepository;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailResponse;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailSearch;
import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.domain.PointStatus;
import com.pointreserve.reserves.point.infra.PointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pointreserve.reserves.point.domain.PointStatus.*;

@SpringBootTest
@ActiveProfiles("local")
class PointDetailServiceTest {

    @Autowired
    private PointDetailRepository pointDetailRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailService pointDetailService;

    @BeforeEach
    void clean(){
        pointRepository.deleteAll();
        pointDetailRepository.deleteAll();
    }

    private PointDetailCreate createEventDetailCreteFactory(Long memberId, PointStatus status, int amount, String eventId, LocalDateTime time){
        return PointDetailCreate.builder()
                .membershipId(memberId)
                .status(status)
                .amount(amount)
                .eventId(eventId)
                .effectiveData(time)
                .expiryDate(time.plusYears(1))
                .build();
    }

    private Point createEventReservesFactory(Long memberId, int amount, PointStatus status){
       return Point.builder().memberId(memberId).amount(amount).status(status).build();
    }

    private List<PointDetail> makeEvetDetailForRedeem(){
        List<PointDetail> pointDetailList = new ArrayList<>();

        Long memberId = 1L;

        Point firstSaveUpEvent = pointRepository.save(createEventReservesFactory(memberId,10,SAVEUP));
        pointDetailList.add(new PointDetailCreate(firstSaveUpEvent).toEntity());

        Point secondSaveUpEvent = pointRepository.save(createEventReservesFactory(memberId,20,SAVEUP));
        pointDetailList.add(new PointDetailCreate(secondSaveUpEvent).toEntity());

        Point thirdSaveUpEvent = pointRepository.save(createEventReservesFactory(memberId,30,SAVEUP));
        pointDetailList.add(new PointDetailCreate(thirdSaveUpEvent).toEntity());

        Point forthSaveUpEvent = pointRepository.save(createEventReservesFactory(memberId,40,SAVEUP));
        pointDetailList.add(new PointDetailCreate(forthSaveUpEvent).toEntity());

        return pointDetailList;
    }

    @Test
    @DisplayName("적립금 적립")
    void saveUpReservesTest() {
        // given
        Long memberId = 1L;
        LocalDateTime testTime = LocalDateTime.of(1982, 7, 13, 14, 25, 00);
        PointDetailCreate pointDetailCreate = createEventDetailCreteFactory(memberId, SAVEUP, 40, "e-1", testTime);

        // when
        String id = pointDetailService.saveUpReserves(pointDetailCreate);
        // then
        PointDetail pointDetail = pointDetailRepository.findById(id).get();
        Assertions.assertEquals(1, pointDetailRepository.count());
        Assertions.assertEquals(pointDetailCreate.getAmount(), pointDetail.getAmount());
        Assertions.assertEquals(pointDetailCreate.getStatus(), SAVEUP);
        Assertions.assertEquals(pointDetailCreate.getEventId(), "e-1");
        Assertions.assertEquals(pointDetailCreate.getEffectiveData(), testTime);
        Assertions.assertEquals(pointDetailCreate.getExpiryDate(), testTime.plusYears(1));
        Assertions.assertNull(pointDetailCreate.getSignUpId());
        Assertions.assertNull(pointDetailCreate.getCancelId());
    }

    @Test
    @DisplayName("적립 내용 취소")
    void cancleSavedUpBefore() {
        // given
        Long memberId = 1L;
        pointDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        int sumAmountBefore =  pointDetailRepository.sumAmount(memberId);
        Point cancelSaveUp = pointRepository.save(createEventReservesFactory(memberId, 0, CANCLE_SAVEUP));
        PointDetailCreate pointDetailCreate2 = new PointDetailCreate(cancelSaveUp);
        // when
        pointDetailService.cancleSavedUpBefore("e-1", pointDetailCreate2);
        int sumAmountAfter =  pointDetailRepository.sumAmount(memberId);
        // then
        Assertions.assertEquals(sumAmountBefore-10, sumAmountAfter);
    }

    @Test
    @DisplayName("적립금 사용 테스트입니다.")
    void redeemReservesTest() {
        // given
        Long memberId = 1L;
        pointDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        Point eventResponse = pointRepository.save(createEventReservesFactory(memberId, 50, REDEEM));
        PointDetailCreate pointDetailCreate = new PointDetailCreate(eventResponse);
        int sumAmountBefore =  pointDetailRepository.sumAmount(memberId);
        // when
        pointDetailService.redeemReserves(pointDetailCreate);
        // then
        int sumAmountAfter = pointDetailRepository.sumAmount(memberId);
        Assertions.assertEquals((sumAmountBefore-50), sumAmountAfter);
        // one more given
        Point eventResponse2 = pointRepository.save(createEventReservesFactory(memberId, 30, REDEEM));
        PointDetailCreate pointDetailCreate2 = new PointDetailCreate(eventResponse2);
        // when
        pointDetailService.redeemReserves(pointDetailCreate2);
        // then
        int sumAmountOneMore = pointDetailRepository.sumAmount(memberId);
        Assertions.assertEquals((sumAmountAfter-30), sumAmountOneMore);
    }

    @Test()
    @DisplayName("사용 취소 테스트")
    void cancelRedeemedBeforeTest() {
        // given
        Long memberId = 1L;
        pointDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        Point eventResponse = pointRepository.save( createEventReservesFactory(memberId, 50, REDEEM) );
        PointDetailCreate pointDetailCreate = new PointDetailCreate(eventResponse);
        int sumAmountBefore =  pointDetailRepository.sumAmount(memberId);
        // when
        pointDetailService.redeemReserves(pointDetailCreate);
        Point cancelRedeem = pointRepository.save(createEventReservesFactory(memberId,0, CANCLE_REDEEM));
        PointDetailCreate pointDetailCreate2 = new PointDetailCreate(cancelRedeem);
        // when
        pointDetailService.cancelRedeemedBefore("e-5", pointDetailCreate2);
        int sumAmountAfter =  pointDetailRepository.sumAmount(memberId);
        // then
        Assertions.assertEquals(sumAmountBefore, sumAmountAfter);
    }

    @Test
    @DisplayName("이벤트 상세 내역 페이지 조회 테스트")
    void getEventDetailListTest() {
        // given
        Long memberId = 1L;
        for(int i = 0; i < 30; i++) {
            Point e = pointRepository.save(createEventReservesFactory(memberId,(i+1),SAVEUP));
            pointDetailRepository.save(new PointDetailCreate(e).toEntity());
        }
        // when
        List<PointDetailResponse> response = pointDetailService.getEventDetailList(PointDetailSearch.builder()
                        .page(0)
                        .size(10)
                        .memberId(memberId)
                    .build());
        // then
        Assertions.assertEquals(response.size(), 10);
    }

}
package com.pointreserve.reserves.eventDetail.application.service;

import com.pointreserve.reserves.eventDetail.application.service.EventDetailService;
import com.pointreserve.reserves.eventDetail.domain.EventDetail;
import com.pointreserve.reserves.eventDetail.infra.EventDetailRepository;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailCreate;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailResponse;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailSearch;
import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.infra.EventReservesRepository;
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

import static com.pointreserve.reserves.eventReserves.domain.ReservesStatus.*;

@SpringBootTest
@ActiveProfiles("local")
class EventDetailServiceTest {

    @Autowired
    private EventDetailRepository eventDetailRepository;

    @Autowired
    private EventReservesRepository eventReservesRepository;

    @Autowired
    private EventDetailService eventDetailService;

    @BeforeEach
    void clean(){
        eventReservesRepository.deleteAll();
        eventDetailRepository.deleteAll();
    }

    @Test
    @DisplayName("적립금 적립")
    void saveUpReservesTest() {
        // given
        Long memberId = 1L;
        LocalDateTime testTime = LocalDateTime.of(1982, 7, 13, 14, 25, 00);
        EventDetailCreate eventDetailCreate = EventDetailCreate.builder()
                .membershipId(memberId)
                .status(SAVEUP)
                .amount(40)
                .eventId("e-1")
                .effectiveData(testTime)
                .expiryDate(testTime.plusYears(1))
                .build();

        // when
        String id = eventDetailService.saveUpReserves(eventDetailCreate);
        // then
        EventDetail eventDetail = eventDetailRepository.findById(id).get();
        Assertions.assertEquals(1, eventDetailRepository.count());
        Assertions.assertEquals(eventDetailCreate.getAmount(), eventDetail.getAmount());
        Assertions.assertEquals(eventDetailCreate.getStatus(), SAVEUP);
        Assertions.assertEquals(eventDetailCreate.getEventId(), "e-1");
        Assertions.assertEquals(eventDetailCreate.getEffectiveData(), testTime);
        Assertions.assertEquals(eventDetailCreate.getExpiryDate(), testTime.plusYears(1));
        Assertions.assertEquals(eventDetailCreate.getSignUpId(), null);
        Assertions.assertEquals(eventDetailCreate.getCancelId(), null);
    }

    @Test
    @DisplayName("적립 내용 취소")
    void cancleSavedUpBefore() {
        // given
        Long memberId = 1L;
        List<EventDetail> given = eventDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
//        EventReserves eventResponse = eventReservesRepository.save(EventReserves.builder()
//                .memberId(memberId)
//                .amount(50)
//                .status(ReservesStatus.REDEEM)
//                .build());
//        EventDetailCreate eventDetailCreate = new EventDetailCreate(eventResponse);
        int sumAmountBefore =  eventDetailRepository.sumAmount(memberId);
//        // when
//        eventDetailService.redeemReserves(eventDetailCreate);

        List<EventDetail> beforeList = eventDetailRepository.findAll();
        System.out.println("before list ==============================");
        for( EventDetail e : beforeList) {
            System.out.println(e.getId() + " " + e.getStatus() + " " + e.getAmount() + " " + e.getSignUpId() + " " + e.getCancelId() + " " + e.getEventId());
        }



        // 적립과 사용이 일어난 상황에서 적립 내용 취소
        EventReserves cancelSaveUp = eventReservesRepository.save(EventReserves.builder()
                .memberId(memberId)
                .status(CANCLE_SAVEUP)
                .build());

        EventDetailCreate eventDetailCreate2 = new EventDetailCreate(cancelSaveUp);
        // when
        eventDetailService.cancleSavedUpBefore("e-1", eventDetailCreate2);
        int sumAmountAfter =  eventDetailRepository.sumAmount(memberId);
        // then

        List<EventDetail> afterList = eventDetailRepository.findAll();
        System.out.println("after list ==============================");
        for( EventDetail e : afterList) {
            System.out.println(e.getId() + " " + e.getStatus() + " " + e.getAmount() + " " + e.getSignUpId() + " " + e.getCancelId() + " " + e.getEventId());
        }

        Assertions.assertEquals(sumAmountBefore-10, sumAmountAfter);
    }

    private List<EventDetail> makeEvetDetailForRedeem(){
        List<EventDetail> eventDetailList = new ArrayList<>();

        Long memberId = 1L;
        LocalDateTime testTime = LocalDateTime.of(1982, 7, 13, 14, 25, 00);

        EventReserves firstSaveUpEvent = eventReservesRepository.save(EventReserves.builder().memberId(memberId).amount(10).status(SAVEUP).build());
        eventDetailList.add(new EventDetailCreate(firstSaveUpEvent).toEntity());

        EventReserves secondSaveUpEvent = eventReservesRepository.save(EventReserves.builder().memberId(memberId).amount(20).status(SAVEUP).build());
        eventDetailList.add(new EventDetailCreate(secondSaveUpEvent).toEntity());

        EventReserves thirdSaveUpEvent = eventReservesRepository.save(EventReserves.builder().memberId(memberId).amount(30).status(SAVEUP).build());
        eventDetailList.add(new EventDetailCreate(thirdSaveUpEvent).toEntity());

        EventReserves forthSaveUpEvent = eventReservesRepository.save(EventReserves.builder().memberId(memberId).amount(40).status(SAVEUP).build());
        eventDetailList.add(new EventDetailCreate(forthSaveUpEvent).toEntity());

        return eventDetailList;
    }


    @Test
    @DisplayName("적립금 사용 테스트입니다.")
    void redeemReservesTest() {
        // given
        Long memberId = 1L;
        List<EventDetail> given = eventDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        EventReserves eventResponse = eventReservesRepository.save(EventReserves.builder()
                .memberId(memberId)
                .amount(50)
                .status(REDEEM)
                .build());
        EventDetailCreate eventDetailCreate = new EventDetailCreate(eventResponse);
        int sumAmountBefore =  eventDetailRepository.sumAmount(memberId);
        // when
        eventDetailService.redeemReserves(eventDetailCreate);
        // then
        int sumAmountAfter = eventDetailRepository.sumAmount(memberId);
        Assertions.assertEquals((sumAmountBefore-50), sumAmountAfter);
        // one more given
        EventReserves eventResponse2 = eventReservesRepository.save(EventReserves.builder()
                .memberId(memberId)
                .amount(30)
                .status(REDEEM)
                .build());
        EventDetailCreate eventDetailCreate2 = new EventDetailCreate(eventResponse2);
        // when
        eventDetailService.redeemReserves(eventDetailCreate2);
        // then
        int sumAmountOneMore = eventDetailRepository.sumAmount(memberId);
        Assertions.assertEquals((sumAmountAfter-30), sumAmountOneMore);
    }

    @Test()
    @DisplayName("사용 취소 테스트")
    void cancelRedeemedBeforeTest() {
        // given
        Long memberId = 1L;
        List<EventDetail> given = eventDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        EventReserves eventResponse = eventReservesRepository.save(EventReserves.builder()
                .memberId(memberId)
                .amount(50)
                .status(REDEEM)
                .build());
        EventDetailCreate eventDetailCreate = new EventDetailCreate(eventResponse);
        int sumAmountBefore =  eventDetailRepository.sumAmount(memberId);
        // when
        eventDetailService.redeemReserves(eventDetailCreate);

        EventReserves cancelRedeem = eventReservesRepository.save(EventReserves.builder()
                .memberId(memberId)
                .status(CANCLE_REDEEM)
                .build());

        EventDetailCreate eventDetailCreate2 = new EventDetailCreate(cancelRedeem);
        // when
        eventDetailService.cancelRedeemedBefore("e-5", eventDetailCreate2);
        int sumAmountAfter =  eventDetailRepository.sumAmount(memberId);
        // then
        Assertions.assertEquals(sumAmountBefore, sumAmountAfter);
    }

    @Test
    @DisplayName("이벤트 상세 내역 페이지 조회 테스트")
    void getEventDetailListTest() {
        // given
        Long memberId = 1L;
        for(int i = 0; i < 30; i++) {
            EventReserves e = eventReservesRepository.save(EventReserves.builder().memberId(memberId).amount(i+1).status(SAVEUP).build());
            eventDetailRepository.save(new EventDetailCreate(e).toEntity());
        }
        // when
        List<EventDetailResponse> response = eventDetailService.getEventDetailList(EventDetailSearch.builder()
                        .page(0)
                        .size(10)
                        .memberId(memberId)
                    .build());
        // then
        Assertions.assertEquals(response.size(), 10);
    }
}
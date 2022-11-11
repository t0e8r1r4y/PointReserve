package com.pointreserve.reserves.eventdetail.application.service;

import com.pointreserve.reserves.eventdetail.domain.EventDetail;
import com.pointreserve.reserves.eventdetail.infra.EventDetailRepository;
import com.pointreserve.reserves.eventdetail.ui.dto.EventDetailCreate;
import com.pointreserve.reserves.eventdetail.ui.dto.EventDetailResponse;
import com.pointreserve.reserves.eventdetail.ui.dto.EventDetailSearch;
import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.domain.ReservesStatus;
import com.pointreserve.reserves.eventreserves.infra.EventReservesRepository;
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

import static com.pointreserve.reserves.eventreserves.domain.ReservesStatus.*;

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

    private EventDetailCreate createEventDetailCreteFactory(Long memberId, ReservesStatus status, int amount, String eventId, LocalDateTime time){
        return EventDetailCreate.builder()
                .membershipId(memberId)
                .status(status)
                .amount(amount)
                .eventId(eventId)
                .effectiveData(time)
                .expiryDate(time.plusYears(1))
                .build();
    }

    private EventReserves createEventReservesFactory(Long memberId, int amount, ReservesStatus status){
       return EventReserves.builder().memberId(memberId).amount(amount).status(status).build();
    }

    private List<EventDetail> makeEvetDetailForRedeem(){
        List<EventDetail> eventDetailList = new ArrayList<>();

        Long memberId = 1L;

        EventReserves firstSaveUpEvent = eventReservesRepository.save(createEventReservesFactory(memberId,10,SAVEUP));
        eventDetailList.add(new EventDetailCreate(firstSaveUpEvent).toEntity());

        EventReserves secondSaveUpEvent = eventReservesRepository.save(createEventReservesFactory(memberId,20,SAVEUP));
        eventDetailList.add(new EventDetailCreate(secondSaveUpEvent).toEntity());

        EventReserves thirdSaveUpEvent = eventReservesRepository.save(createEventReservesFactory(memberId,30,SAVEUP));
        eventDetailList.add(new EventDetailCreate(thirdSaveUpEvent).toEntity());

        EventReserves forthSaveUpEvent = eventReservesRepository.save(createEventReservesFactory(memberId,40,SAVEUP));
        eventDetailList.add(new EventDetailCreate(forthSaveUpEvent).toEntity());

        return eventDetailList;
    }

    @Test
    @DisplayName("적립금 적립")
    void saveUpReservesTest() {
        // given
        Long memberId = 1L;
        LocalDateTime testTime = LocalDateTime.of(1982, 7, 13, 14, 25, 00);
        EventDetailCreate eventDetailCreate = createEventDetailCreteFactory(memberId, SAVEUP, 40, "e-1", testTime);

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
        Assertions.assertNull(eventDetailCreate.getSignUpId());
        Assertions.assertNull(eventDetailCreate.getCancelId());
    }

    @Test
    @DisplayName("적립 내용 취소")
    void cancleSavedUpBefore() {
        // given
        Long memberId = 1L;
        eventDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        int sumAmountBefore =  eventDetailRepository.sumAmount(memberId);
        EventReserves cancelSaveUp = eventReservesRepository.save(createEventReservesFactory(memberId, 0, CANCLE_SAVEUP));
        EventDetailCreate eventDetailCreate2 = new EventDetailCreate(cancelSaveUp);
        // when
        eventDetailService.cancleSavedUpBefore("e-1", eventDetailCreate2);
        int sumAmountAfter =  eventDetailRepository.sumAmount(memberId);
        // then
        Assertions.assertEquals(sumAmountBefore-10, sumAmountAfter);
    }

    @Test
    @DisplayName("적립금 사용 테스트입니다.")
    void redeemReservesTest() {
        // given
        Long memberId = 1L;
        eventDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        EventReserves eventResponse = eventReservesRepository.save(createEventReservesFactory(memberId, 50, REDEEM));
        EventDetailCreate eventDetailCreate = new EventDetailCreate(eventResponse);
        int sumAmountBefore =  eventDetailRepository.sumAmount(memberId);
        // when
        eventDetailService.redeemReserves(eventDetailCreate);
        // then
        int sumAmountAfter = eventDetailRepository.sumAmount(memberId);
        Assertions.assertEquals((sumAmountBefore-50), sumAmountAfter);
        // one more given
        EventReserves eventResponse2 = eventReservesRepository.save(createEventReservesFactory(memberId, 30, REDEEM));
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
        eventDetailRepository.saveAll(makeEvetDetailForRedeem()); // 기존 적립
        EventReserves eventResponse = eventReservesRepository.save( createEventReservesFactory(memberId, 50, REDEEM) );
        EventDetailCreate eventDetailCreate = new EventDetailCreate(eventResponse);
        int sumAmountBefore =  eventDetailRepository.sumAmount(memberId);
        // when
        eventDetailService.redeemReserves(eventDetailCreate);
        EventReserves cancelRedeem = eventReservesRepository.save(createEventReservesFactory(memberId,0, CANCLE_REDEEM));
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
            EventReserves e = eventReservesRepository.save(createEventReservesFactory(memberId,(i+1),SAVEUP));
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
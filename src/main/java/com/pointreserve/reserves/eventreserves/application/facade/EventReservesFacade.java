package com.pointreserve.reserves.eventreserves.application.facade;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointEdit;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.common.event.EventPublisher;
import com.pointreserve.reserves.eventdetail.ui.dto.EventDetailCreate;
import com.pointreserve.reserves.eventreserves.application.service.EventReservesService;
import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.domain.ReservesStatus;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesCancel;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesCreate;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.lang.Math.abs;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReservesFacade {

    private final EventPublisher publisher;

    private final EventReservesService eventReservesService;

    private final AccumulatedPointService accumulatedPointService;

    @ExceptionHandler(AccumulatedPointInvalidRequestException.class)
    public EventReservesResponse createEventReserves(EventReservesCreate eventReservesCreate) {

        EventReserves eventReserves = eventReservesCreate.toEntity();
        // 이벤트 저장
        EventReserves saveResult = eventReservesService.saveEventReserves(eventReserves);
        // 총계 업데이트
        accumulatedPointService.calcPointAndUpdate(saveResult.getMemberId(), saveResult.getAmount(), saveResult.getStatus());
        // 이벤트 발행
        EventDetailCreate eventDetailCreate = new EventDetailCreate(saveResult);
        eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.STANDBY);
        eventDetailCreate.setBeforeHistoryId(null);
        publisher.publish(eventDetailCreate);

        return EventReservesResponse.builder()
                .eventReserves(saveResult)
                .build();
    }

    public EventReservesResponse createCancelEventReserves(EventReservesCancel eventReservesCancel){
        String beforeEventId = eventReservesCancel.getEventId();
        EventReserves eventReserves = eventReservesCancel.toEntity();
        // 이전 정보 조회
        EventReservesResponse beforeHistory = eventReservesService.getEventReserves(beforeEventId);
        // 업데이트 대상 조회
        AccumulatedPointResponse accumulatedPointResponse = accumulatedPointService.getAccumulatedPoint(eventReserves.getMemberId());
        // 금액 계산
        AccumulatedPointEdit accumulatedPointEdit = AccumulatedPointEdit.builder()
                .totalAmount( accumulatedPointResponse.getTotalAmount() + abs(beforeHistory.getAmount()) )
                .build();
        // 금액 유효성 검사
        accumulatedPointEdit.isValid();
        // 업데이트 요청
//        accumulatedPointService.updateAccumulatedPoint(eventReserves.getMemberId(), accumulatedPointEdit);
        // 저장
        EventReserves saveResult = eventReservesService.saveEventReserves(eventReserves);
        // 이벤트 발행
        EventDetailCreate eventDetailCreate = new EventDetailCreate(saveResult);
        eventDetailCreate.setBeforeHistoryId(beforeEventId);
        eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.STANDBY);
        publisher.publish(eventDetailCreate);

        return EventReservesResponse.builder()
                .eventReserves(saveResult)
                .build();
    }

    private int calUpdateAmount(EventReserves e, int beforeTotalAmount) {
        return ( (e.getStatus() == ReservesStatus.SAVEUP) ? e.getAmount() : e.getAmount()*(-1) ) + beforeTotalAmount;
    }

}
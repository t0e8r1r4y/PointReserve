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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.pointreserve.reserves.eventdetail.ui.dto.EventDetailCreate.EventStatus.STANDBY;
import static java.lang.Math.abs;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReservesFacade {

    private final EventPublisher publisher;

    private final EventReservesService eventReservesService;

    private final AccumulatedPointService accumulatedPointService;

    @Transactional
    public EventReservesResponse createEventReserves(EventReservesCreate eventReservesCreate) {

        EventReserves eventReserves = eventReservesCreate.toEntity();
        // 이벤트 저장
        EventReserves saveResult = eventReservesService.saveEventReserves(eventReserves);
        // 총계 업데이트
        accumulatedPointService.calcPointAndUpdate(saveResult.getMemberId(), saveResult.getAmount(), saveResult.getStatus());
        // 이벤트 발행
        EventDetailCreate eventDetailCreate = new EventDetailCreate(saveResult);
        eventDetailCreate.updateEventStatus(STANDBY);
        eventDetailCreate.setBeforeHistoryId(null);
        publisher.publish(eventDetailCreate);

        return EventReservesResponse.builder()
                .eventReserves(saveResult)
                .build();
    }

    @Transactional
    public EventReservesResponse createCancelEventReserves(EventReservesCancel eventReservesCancel){

        EventReserves eventReserves = eventReservesCancel.toEntity();
        // 이벤트 저장
        EventReserves saveResult = eventReservesService.saveEventReserves(eventReserves);
        // 이전 정보 조회
        EventReservesResponse beforeHistory = eventReservesService.getEventReserves(eventReservesCancel.getEventId());
        // 총계 업데이트
        accumulatedPointService.calcPointAndUpdate(saveResult.getMemberId(), abs(beforeHistory.getAmount()), saveResult.getStatus());
        // 이벤트 발행
        EventDetailCreate eventDetailCreate = new EventDetailCreate(saveResult);
        eventDetailCreate.setBeforeHistoryId(eventReservesCancel.getEventId());
        eventDetailCreate.updateEventStatus(STANDBY);
        publisher.publish(eventDetailCreate);

        return EventReservesResponse.builder()
                .eventReserves(saveResult)
                .build();
    }

}

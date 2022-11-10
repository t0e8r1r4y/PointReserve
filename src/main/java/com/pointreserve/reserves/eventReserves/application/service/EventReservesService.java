package com.pointreserve.reserves.eventReserves.application.service;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointEdit;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.common.component.EventPublisher;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailCreate;
import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.infra.EventReservesRepository;
import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import com.pointreserve.reserves.eventReserves.exception.EventReservesNotFound;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCancel;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCreate;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesResponse;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReservesService {
    private final EventPublisher publisher;
    private final EventReservesRepository eventReservesRepository;

    private final AccumulatedPointService accumulatedPointService;

    @Transactional
    public EventReserves saveEventReserves(EventReserves e) {
        return eventReservesRepository.save(e);
    }

    // 테스트 외 사용안함
    @Transactional
    public EventReservesResponse createEventReserves(EventReservesCreate eventReservesCreate) {

        EventReserves eventReserves = eventReservesCreate.toEntity();

        // 업데이트 대상 조회
        AccumulatedPointResponse accumulatedPointResponse = accumulatedPointService.getAccumulatedPoint(eventReserves.getMemberId());
        // 금액 계산
        AccumulatedPointEdit accumulatedPointEdit = AccumulatedPointEdit.builder()
                .totalAmount(calUpdateAmount( eventReserves, accumulatedPointResponse.getTotalAmount() ))
                .build();
        // 금액 유효성 검사
        accumulatedPointEdit.isValid();
        // 업데이트 요청
//        accumulatedPointService.updateAccumulatedPoint(eventReserves.getMemberId(), accumulatedPointEdit);
        // 저장
        EventReserves saveResult = eventReservesRepository.save(eventReserves);
        // 이벤트 발행
        EventDetailCreate eventDetailCreate = new EventDetailCreate(saveResult);
        eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.STANDBY);
        eventDetailCreate.setBeforeHistoryId(null);
        publisher.publish(eventDetailCreate);

        return EventReservesResponse.builder()
                .eventReserves(saveResult)
                .build();
    }

    // 테스트 외 사용안함
    @Transactional
    public EventReservesResponse createCancelEventReserves(EventReservesCancel eventReservesCancel){
        String beforeEventId = eventReservesCancel.getEventId();
        EventReserves eventReserves = eventReservesCancel.toEntity();

        EventReserves beforeHistory = eventReservesRepository.findById(beforeEventId)
                .orElseThrow(()->{throw new EventReservesNotFound();});


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
        EventReserves saveResult = eventReservesRepository.save(eventReserves);
        // 이벤트 발행
        EventDetailCreate eventDetailCreate = new EventDetailCreate(saveResult);
        eventDetailCreate.setBeforeHistoryId(beforeEventId);
        eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.STANDBY);
        publisher.publish(eventDetailCreate);

        return EventReservesResponse.builder()
                .eventReserves(saveResult)
                .build();
    }

    @Transactional(readOnly = true)
    public EventReservesResponse getEventReserves(String eventId){
        EventReserves eventReserves = eventReservesRepository.findById(eventId).orElseThrow(
                () -> new EventReservesNotFound()
        );
        return EventReservesResponse.builder().eventReserves(eventReserves).build();
    }

    public List<EventReservesResponse> getEventReservesList(EventReservesSearch eventReservesSearch){
        List<EventReservesResponse> responseList = eventReservesRepository.getList(eventReservesSearch)
                .stream().map(EventReservesResponse::new)
                .collect(Collectors.toList());
        if(responseList.isEmpty()){
            throw new EventReservesNotFound();
        }
        return responseList;
    }


    private int calUpdateAmount( EventReserves e, int beforeTotalAmount ) {
        return ( (e.getStatus() == ReservesStatus.SAVEUP) ? e.getAmount() : e.getAmount()*(-1) ) + beforeTotalAmount;
    }
}
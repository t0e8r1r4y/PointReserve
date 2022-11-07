package com.pointreserve.reserves.eventReserves.application.service;

import com.pointreserve.reserves.account.application.service.AccountService;
import com.pointreserve.reserves.account.ui.dto.AccountEdit;
import com.pointreserve.reserves.account.ui.dto.AccountResponse;
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

    private final AccountService accountService;

    @Transactional
    public EventReservesResponse createEventReserves(EventReservesCreate eventReservesCreate) {

        EventReserves eventReserves = eventReservesCreate.toEntity();

        // 업데이트 대상 조회
        AccountResponse accountResponse = accountService.getAccount(eventReserves.getMemberId());
        // 금액 계산
        AccountEdit accountEdit = AccountEdit.builder()
                .totalAmount(calUpdateAmount( eventReserves, accountResponse.getTotalAmount() ))
                .build();
        // 금액 유효성 검사
        accountEdit.isValid();
        // 업데이트 요청
        accountService.updateAccount(eventReserves.getMemberId(), accountEdit);
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

    @Transactional
    public EventReservesResponse createCancelEventReserves(EventReservesCancel eventReservesCancel){
        String beforeEventId = eventReservesCancel.getEventId();
        EventReserves eventReserves = eventReservesCancel.toEntity();

        EventReserves beforeHistory = eventReservesRepository.findById(beforeEventId)
                .orElseThrow(()->{throw new EventReservesNotFound();});


        // 업데이트 대상 조회
        AccountResponse accountResponse = accountService.getAccount(eventReserves.getMemberId());
        // 금액 계산
        AccountEdit accountEdit = AccountEdit.builder()
                .totalAmount( accountResponse.getTotalAmount() + abs(beforeHistory.getAmount()) )
                .build();
        // 금액 유효성 검사
        accountEdit.isValid();
        // 업데이트 요청
        accountService.updateAccount(eventReserves.getMemberId(), accountEdit);
        // 저장
        EventReserves saveResult = eventReservesRepository.save(eventReserves);
        // 이벤트 발행
        EventDetailCreate eventDetailCreate = new EventDetailCreate(saveResult);
//        eventDetailCreate.setEventId(eventReservesCancel.getEventId());
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
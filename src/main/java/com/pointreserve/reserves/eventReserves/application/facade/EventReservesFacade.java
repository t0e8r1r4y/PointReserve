package com.pointreserve.reserves.eventReserves.application.facade;

import com.pointreserve.reserves.account.application.service.AccountService;
import com.pointreserve.reserves.account.ui.dto.AccountEdit;
import com.pointreserve.reserves.account.ui.dto.AccountResponse;
import com.pointreserve.reserves.common.component.EventPublisher;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailCreate;
import com.pointreserve.reserves.eventReserves.application.service.EventReservesService;
import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCancel;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCreate;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.Math.abs;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReservesFacade {

    private final EventPublisher publisher;

    private final EventReservesService eventReservesService;

    private final AccountService accountService;

    public EventReservesResponse createEventReserves(EventReservesCreate eventReservesCreate) {

        EventReserves eventReserves = eventReservesCreate.toEntity();

        AccountResponse accountResponse = accountService.getAccount(eventReserves.getMemberId());

        // 금액 계산
        AccountEdit accountEdit = AccountEdit.builder()
                .totalAmount(calUpdateAmount( eventReserves, accountResponse.getTotalAmount() ))
                .build();
        // 금액 유효성 검사
        accountEdit.isValid();
        // 업데이트 요청
        accountService.updateAccount(eventReserves.getMemberId(), accountEdit);
        // 이벤트 저장
        EventReserves saveResult = eventReservesService.saveEventReserves(eventReserves);
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

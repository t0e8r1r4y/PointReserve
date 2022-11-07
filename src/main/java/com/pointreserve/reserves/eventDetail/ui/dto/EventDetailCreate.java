package com.pointreserve.reserves.eventDetail.ui.dto;

import com.pointreserve.reserves.eventDetail.domain.EventDetail;
import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import lombok.*;

import java.time.LocalDateTime;

import static com.pointreserve.reserves.eventDetail.ui.dto.EventDetailCreate.EventStatus.STANDBY;

@ToString
@Setter
@Getter
public class EventDetailCreate {

    private Long membershipId;
    private ReservesStatus status;
    private int amount;
    private String eventId;
    private String signUpId;
    private String cancelId;
    private LocalDateTime effectiveData;
    private LocalDateTime expiryDate;
    private String beforeHistoryId;
    private EventStatus eventStatus;

    @Builder
    public EventDetailCreate(Long membershipId, ReservesStatus status, int amount, String eventId, String signUpId,
                             String cancelId, LocalDateTime effectiveData, LocalDateTime expiryDate, String beforeHistoryId){
        this.membershipId = membershipId;
        this.status = status;
        this.amount = amount;
        this.eventId = eventId;
        this.signUpId = signUpId;
        this.cancelId = cancelId;
        this.effectiveData = effectiveData;
        this.expiryDate = expiryDate;
        this.beforeHistoryId = beforeHistoryId;
        this.eventStatus = STANDBY;
    }

    public EventDetailCreate( EventReserves eventReserves ){
        this.membershipId = eventReserves.getMemberId();
        this.status = eventReserves.getStatus();
        this.amount = eventReserves.getAmount();
        this.eventId = eventReserves.getId();
        this.effectiveData = eventReserves.getEffectiveData();
        this.expiryDate = effectiveData.plusYears(1);
        this.signUpId = null;
        this.cancelId = null;
        this.eventStatus = STANDBY;
    }

    public EventDetail toEntity(){
        return EventDetail.builder()
                .membershipId(membershipId)
                .status(status)
                .amount(amount)
                .eventId(eventId)
                .signUpId(signUpId)
                .cancelId(cancelId)
                .effectiveData(effectiveData)
                .expiryDate(expiryDate)
                .build();
    }

    public void updateStatusAsRedeem(String signUpId){
        this.signUpId = signUpId;
    }

    public void updateStatusAsCancel(String cancelId){
        this.cancelId = cancelId;
    }

    public void updateEventStatus(EventStatus eventStatus){
        this.eventStatus = eventStatus;
    }

    public void setBeforeHistoryId(String historyId){
        this.beforeHistoryId = historyId;
    }

    public boolean isStandby()
    {
        return eventStatus == STANDBY;
    }

    public boolean isQueueWait()
    {
        return eventStatus == EventStatus.QUEUE_WAIT;
    }

    public enum EventStatus {
        STANDBY,
        QUEUE_WAIT,
        QUEUE,
        PROGRESS,
        SUCCESS,
        FAILURE
    }
}


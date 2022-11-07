package com.pointreserve.reserves.eventDetail.ui.dto;

import com.pointreserve.reserves.eventDetail.domain.EventDetail;
import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventDetailResponse {

    private final String id;
    private final Long membershipId;
    private final ReservesStatus status;
    private final int amount;
    private final String eventId;
    private final String signUpId;
    private final String cancelId;
    private final LocalDateTime effectiveData;
    private final LocalDateTime expiryDate;

    @Builder
    public EventDetailResponse( EventDetail eventDetail ) {
        this.id = eventDetail.getId();
        this.membershipId = eventDetail.getMembershipId();
        this.status = eventDetail.getStatus();
        this.amount = eventDetail.getAmount();
        this.eventId = eventDetail.getEventId();
        this.signUpId = eventDetail.getSignUpId();
        this.cancelId = eventDetail.getCancelId();
        this.effectiveData = eventDetail.getEffectiveData();
        this.expiryDate = eventDetail.getExpiryDate();
    }
}
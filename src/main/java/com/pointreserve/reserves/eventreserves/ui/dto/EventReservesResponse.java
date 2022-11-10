package com.pointreserve.reserves.eventreserves.ui.dto;

import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.domain.ReservesStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class EventReservesResponse {

    private final String id;
    private final Long memberId;
    private final int amount;

    private final ReservesStatus status;

    private final String effectiveData;
    private final String expiryDate;

    @Builder
    public EventReservesResponse( EventReserves eventReserves) {
        this.id = eventReserves.getId();
        this.memberId = eventReserves.getMemberId();
        this.amount = eventReserves.getAmount();
        this.status = eventReserves.getStatus();
        this.effectiveData = toStringDateTime(eventReserves.getEffectiveData());
        this.expiryDate = toStringDateTime(eventReserves.getEffectiveData().plusYears(1));
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}
package com.pointreserve.reserves.point.ui.dto;

import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.domain.PointStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class PointResponse {

    private final String id;
    private final Long memberId;
    private final int amount;

    private final PointStatus status;

    private final String effectiveData;
    private final String expiryDate;

    @Builder
    public PointResponse(Point point) {
        this.id = point.getId();
        this.memberId = point.getMemberId();
        this.amount = point.getAmount();
        this.status = point.getStatus();
        this.effectiveData = toStringDateTime(point.getEffectiveData());
        this.expiryDate = toStringDateTime(point.getEffectiveData().plusYears(1));
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}
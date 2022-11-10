package com.pointreserve.reserves.accumulationpoint.ui.dto;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Setter
@Getter
public class AccumulatedPointCreate {
    @NotNull(message = "회원번호는 필수 입력 항목입니다.")
    private Long memberId;

    @Builder
    public AccumulatedPointCreate(Long memberId) {
        this.memberId = memberId;
    }

    public AccumulatedPoint toEntity() {
        return AccumulatedPoint.builder()
                .memberId(memberId)
                .totalAmount(0)
                .build();
    }
}

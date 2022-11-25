package com.pointreserve.reserves.point.ui.dto;

import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.domain.PointStatus;
import com.pointreserve.reserves.point.exception.PointInvalideRequestException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Setter
@Getter
public class PointCreate {

    @NotNull(message = "회원번호는 필수 입력항목입니다.")
    private Long memberId;

    @NotNull(message = "금액은 필수 입력항목입니다.")
    private int amount;

    @NotNull(message = "거래 상태는 필수 입력항목입니다.")
    private PointStatus pointStatus;

    @Builder
    public PointCreate(Long memberId, int amount, PointStatus pointStatus) {
        this.memberId = memberId;
        this.amount = amount;
        this.pointStatus = pointStatus;
    }

    public Point toEntity(){
        return Point.builder()
                .memberId(memberId)
                .amount(amount)
                .status(pointStatus)
                .build();
    }

    public void isAmountValid() {
        if( amount <= 0 ) {
            throw new PointInvalideRequestException("amount", "금액은 0원 이하가 될 수 없습니다.");
        } else if( amount > 1000000 ) {
            throw new PointInvalideRequestException("amount", "적립금액은 1,000,000원을 초과 할 수 없습니다.");
        }
    }

    public void isStatusValid(){
        if(pointStatus != PointStatus.SAVEUP && pointStatus != PointStatus.REDEEM) {
            throw new PointInvalideRequestException("status", "해당 요청은 적립과 사용만 입력 할 수 있습니다.");
        }
    }
}
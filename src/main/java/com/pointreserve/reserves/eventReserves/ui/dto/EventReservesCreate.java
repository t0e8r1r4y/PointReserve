package com.pointreserve.reserves.eventReserves.ui.dto;

import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import com.pointreserve.reserves.eventReserves.exception.EventReserveInvalideRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Setter
@Getter
public class EventReservesCreate {

    @NotNull(message = "회원번호는 필수 입력항목입니다.")
    private Long memberId;

    @NotNull(message = "금액은 필수 입력항목입니다.")
    private int amount;

    @NotNull(message = "거래 상태는 필수 입력항목입니다.")
    private ReservesStatus reservesStatus;

    @Builder
    public EventReservesCreate(Long memberId, int amount, ReservesStatus reservesStatus) {
        this.memberId = memberId;
        this.amount = amount;
        this.reservesStatus = reservesStatus;
    }

    public EventReserves toEntity(){
        return EventReserves.builder()
                .memberId(memberId)
                .amount(amount)
                .status(reservesStatus)
                .build();
    }

    public void isAmountValid() {
        if( amount <= 0 ) {
            throw new EventReserveInvalideRequest("amount", "금액은 0원 이하가 될 수 없습니다.");
        } else if( amount > 1000000 ) {
            throw new EventReserveInvalideRequest("amount", "적립금액은 1,000,000원을 초과 할 수 없습니다.");
        }
    }

    public void isStatusValid(){
        if(reservesStatus != ReservesStatus.SAVEUP && reservesStatus != ReservesStatus.REDEEM) {
            throw new EventReserveInvalideRequest("status", "해당 요청은 적립과 사용만 입력 할 수 있습니다.");
        }
    }
}
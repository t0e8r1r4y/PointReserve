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
public class EventReservesCancel {

    @NotNull(message = "회원번호는 필수 입력항목입니다.")
    private Long memberId;

    @NotNull(message = "취소와 관련해서 이벤트 번호는 필수입니다.")
    private String eventId;

    @NotNull(message = "거래 상태는 필수 입력항목입니다.")
    private ReservesStatus reservesStatus;


    @Builder
    public EventReservesCancel(Long memberId, String eventId, ReservesStatus reservesStatus){
        this.memberId = memberId;
        this.eventId = eventId;
        this.reservesStatus = reservesStatus;
    }

    public EventReserves toEntity(){
        return EventReserves.builder()
                .memberId(memberId)
                .amount(0)
                .status(reservesStatus)
                .build();
    }

    public void isStatusValid(){
        if(reservesStatus != ReservesStatus.CANCLE_REDEEM) {
            throw new EventReserveInvalideRequest("status", "현재는 적립금 사용 취소만 가능합니다.");
        }
    }
}

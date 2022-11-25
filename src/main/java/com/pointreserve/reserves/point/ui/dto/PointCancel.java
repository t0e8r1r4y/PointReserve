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
public class PointCancel {

    @NotNull(message = "회원번호는 필수 입력항목입니다.")
    private Long memberId;

    @NotNull(message = "취소와 관련해서 이벤트 번호는 필수입니다.")
    private String eventId;

    @NotNull(message = "거래 상태는 필수 입력항목입니다.")
    private PointStatus pointStatus;


    @Builder
    public PointCancel(Long memberId, String eventId, PointStatus pointStatus){
        this.memberId = memberId;
        this.eventId = eventId;
        this.pointStatus = pointStatus;
    }

    public Point toEntity(){
        return Point.builder()
                .memberId(memberId)
                .amount(0)
                .status(pointStatus)
                .build();
    }

    public void isStatusValid(){
        if(pointStatus != PointStatus.CANCLE_REDEEM) {
            throw new PointInvalideRequestException("status", "현재는 적립금 사용 취소만 가능합니다.");
        }
    }
}

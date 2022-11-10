package com.pointreserve.reserves.accumulationpoint.ui.dto;

import com.pointreserve.reserves.accumulationpoint.exception.AccountInvalidRequestException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AccumulatedPointEdit {

    @NotNull(message = "금액은 필수 입력사항입니다.")
    private int totalAmount;

    @Builder
    public AccumulatedPointEdit(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void isValid(){
        if(totalAmount < 0) {
            throw new AccountInvalidRequestException("totalAmount","금액은 0원 미만이 될 수 없습니다.");
        }
    }
}

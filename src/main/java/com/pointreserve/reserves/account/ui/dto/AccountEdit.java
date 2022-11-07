package com.pointreserve.reserves.account.ui.dto;

import com.pointreserve.reserves.account.exception.AccountInvalidRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AccountEdit {

    @NotNull(message = "금액은 필수 입력사항입니다.")
    private int totalAmount;

    @Builder
    public AccountEdit(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void isValid(){
        if(totalAmount < 0) {
            throw new AccountInvalidRequest("totalAmount","금액은 0원 미만이 될 수 없습니다.");
        }
    }
}

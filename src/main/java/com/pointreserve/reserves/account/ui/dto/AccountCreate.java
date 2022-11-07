package com.pointreserve.reserves.account.ui.dto;

import com.pointreserve.reserves.account.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Setter
@Getter
public class AccountCreate {
    @NotNull(message = "회원번호는 필수 입력 항목입니다.")
    private Long memberId;

    @Builder
    public AccountCreate(Long memberId) {
        this.memberId = memberId;
    }

    public Account toEntity() {
        return Account.builder()
                .memberId(memberId)
                .totalAmount(0)
                .build();
    }
}

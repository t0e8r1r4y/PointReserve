package com.pointreserve.reserves.account.ui.dto;

import com.pointreserve.reserves.account.domain.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountResponse {
    private final Long id;
    private final Long memberId;
    private final int totalAmount;

    @Builder
    public AccountResponse(Account account){
        this.id = account.getId();
        this.memberId = account.getMemberId();
        this.totalAmount = account.getTotalAmount();
    }

    public AccountResponse(Long id, Long memberId, int totalAmount){
        this.id = id;
        this.memberId = memberId;
        this.totalAmount = totalAmount;
    }
}

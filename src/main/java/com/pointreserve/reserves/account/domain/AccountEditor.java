package com.pointreserve.reserves.account.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountEditor {
    private final int totalAmount;

    @Builder
    public AccountEditor(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}

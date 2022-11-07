package com.pointreserve.reserves.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long memberId;


    @Column(nullable = false)
    private int totalAmount;

    @Builder
    public Account(Long memberId, int totalAmount) {
        this.memberId = memberId;
        this.totalAmount = totalAmount;
    }


    public AccountEditor.AccountEditorBuilder toEditor(){
        return AccountEditor.builder().totalAmount(totalAmount);
    }

    public void edit(AccountEditor reservesAmountEdit) {
        this.totalAmount = reservesAmountEdit.getTotalAmount();
    }
}

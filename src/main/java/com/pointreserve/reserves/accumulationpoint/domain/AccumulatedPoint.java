package com.pointreserve.reserves.accumulationpoint.domain;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointEditor.AccumulatedPointEditorBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "ACCOUNT")
public class AccumulatedPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;


    @Column(nullable = false)
    private int totalAmount;

    @Builder
    public AccumulatedPoint(Long memberId, int totalAmount) {
        this.memberId = memberId;
        this.totalAmount = totalAmount;
    }


    public AccumulatedPointEditorBuilder toEditor(){
        return AccumulatedPointEditor.builder().totalAmount(totalAmount);
    }

    public void edit(AccumulatedPointEditor reservesAmountEdit) {
        this.totalAmount = reservesAmountEdit.getTotalAmount();
    }
}

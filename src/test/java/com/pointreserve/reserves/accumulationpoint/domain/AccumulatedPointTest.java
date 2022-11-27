package com.pointreserve.reserves.accumulationpoint.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointEditor.*;
import static org.junit.jupiter.api.Assertions.*;

class AccumulatedPointTest {

    AccumulatedPointFactoryImpl factory = new AccumulatedPointFactoryImpl();

    @Test
    @DisplayName("누적 포인트 엔티티 build 패턴으로 생성 테스트")
    void toEditor() {
        // given
        AccumulatedPoint given = factory.createAccumulatedPoint(1L,100);

        // when
        AccumulatedPointEditorBuilder amountEditorBuilder = given.toEditorBuilder();

        // then
        assertEquals(given.getTotalAmount(), amountEditorBuilder.build().getTotalAmount());
    }

    @Test
    @DisplayName("누적 포인트 엔티티 전체 금액 업데이트")
    void edit() {
        // given
        AccumulatedPoint given = factory.createAccumulatedPoint(1L,100);
        AccumulatedPointEditor givenEditor =  factory.createAccumulatedPointEditor(150);

        // when
        given.edit(givenEditor);

        // then
        assertEquals(given.getTotalAmount(), givenEditor.getTotalAmount());
    }
}
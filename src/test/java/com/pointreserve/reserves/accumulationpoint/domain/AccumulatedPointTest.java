package com.pointreserve.reserves.accumulationpoint.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointEditor.*;
import static org.junit.jupiter.api.Assertions.*;

class AccumulatedPointTest {

    AccumulatedPointFactoryImpl factory = new AccumulatedPointFactoryImpl();

    @Test
    @DisplayName("AccumulatedPoint형 객체가 주어졌을 때, builder 생성 및 build() 동작 확인 테스트")
    void toEditor() {
        // given
        AccumulatedPoint given = factory.createAccumulatedPoint(1L,100);

        // when
        AccumulatedPointEditorBuilder amountEditorBuilder = given.toEditor();

        // then
        assertEquals(given.getTotalAmount(), amountEditorBuilder.build().getTotalAmount());
    }

    @Test
    @DisplayName("AccumulatedPoint class의 total amount 변경 메서드 테스트")
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
package com.pointreserve.reserves.accumulationpoint.domain;

public class AccumulatedPointFactoryImpl implements AccumulatedPointFactory {
    @Override
    public AccumulatedPoint createAccumulatedPoint(Long memberId, int totalAmount) {
        return AccumulatedPoint.builder()
                .memberId(memberId)
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    public AccumulatedPointEditor createAccumulatedPointEditor(int totalAmount) {
        return AccumulatedPointEditor.builder()
                .totalAmount(totalAmount)
                .build();
    }
}

package com.pointreserve.reserves.accumulationpoint.domain;

public interface AccumulatedPointFactory {
    AccumulatedPoint createAccumulatedPoint(Long memberId, int totalAmount);
    AccumulatedPointEditor createAccumulatedPointEditor(int totalAmount);
}

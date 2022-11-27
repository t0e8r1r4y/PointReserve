package com.pointreserve.reserves.accumulationpoint.ui.dto;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccumulatedPointResponse {

  private final Long id;
  private final Long memberId;
  private final int totalAmount;

  @Builder
  public AccumulatedPointResponse(AccumulatedPoint accumulatedPoint) {
    this.id = accumulatedPoint.getId();
    this.memberId = accumulatedPoint.getMemberId();
    this.totalAmount = accumulatedPoint.getTotalAmount();
  }

  public AccumulatedPointResponse(Long id, Long memberId, int totalAmount) {
    this.id = id;
    this.memberId = memberId;
    this.totalAmount = totalAmount;
  }
}

package com.pointreserve.reserves.accumulationpoint.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccumulatedPointEditor {

  private final int totalAmount;

  @Builder
  public AccumulatedPointEditor(int totalAmount) {
    this.totalAmount = totalAmount;
  }
}

package com.pointreserve.reserves.pointdetail.ui.dto;

import com.pointreserve.reserves.pointdetail.domain.PointDetail;
import com.pointreserve.reserves.point.domain.PointStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PointDetailResponse {

  private final String id;
  private final Long membershipId;
  private final PointStatus status;
  private final int amount;
  private final String eventId;
  private final String signUpId;
  private final String cancelId;
  private final LocalDateTime effectiveData;
  private final LocalDateTime expiryDate;

  @Builder
  public PointDetailResponse(PointDetail pointDetail) {
    this.id = pointDetail.getId();
    this.membershipId = pointDetail.getMembershipId();
    this.status = pointDetail.getStatus();
    this.amount = pointDetail.getAmount();
    this.eventId = pointDetail.getEventId();
    this.signUpId = pointDetail.getSignUpId();
    this.cancelId = pointDetail.getCancelId();
    this.effectiveData = pointDetail.getEffectiveData();
    this.expiryDate = pointDetail.getExpiryDate();
  }
}
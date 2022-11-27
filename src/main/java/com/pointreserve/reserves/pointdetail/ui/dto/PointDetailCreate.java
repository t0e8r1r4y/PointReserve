package com.pointreserve.reserves.pointdetail.ui.dto;

import com.pointreserve.reserves.pointdetail.domain.PointDetail;
import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.domain.PointStatus;
import lombok.*;

import java.time.LocalDateTime;

import static com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate.EventStatus.STANDBY;

@ToString
@Setter
@Getter
public class PointDetailCreate {

  private Long membershipId;
  private PointStatus status;
  private int amount;
  private String eventId;
  private String signUpId;
  private String cancelId;
  private LocalDateTime effectiveData;
  private LocalDateTime expiryDate;
  private String beforeHistoryId;
  private EventStatus eventStatus;

  @Builder
  public PointDetailCreate(Long membershipId, PointStatus status, int amount, String eventId,
      String signUpId,
      String cancelId, LocalDateTime effectiveData, LocalDateTime expiryDate,
      String beforeHistoryId) {
    this.membershipId = membershipId;
    this.status = status;
    this.amount = amount;
    this.eventId = eventId;
    this.signUpId = signUpId;
    this.cancelId = cancelId;
    this.effectiveData = effectiveData;
    this.expiryDate = expiryDate;
    this.beforeHistoryId = beforeHistoryId;
    this.eventStatus = STANDBY;
  }

  public PointDetailCreate(Point point) {
    this.membershipId = point.getMemberId();
    this.status = point.getStatus();
    this.amount = point.getAmount();
    this.eventId = point.getId();
    this.effectiveData = point.getEffectiveData();
    this.expiryDate = effectiveData.plusYears(1);
    this.signUpId = null;
    this.cancelId = null;
    this.eventStatus = STANDBY;
  }

  public PointDetail toEntity() {
    return PointDetail.builder()
        .membershipId(membershipId)
        .status(status)
        .amount(amount)
        .eventId(eventId)
        .signUpId(signUpId)
        .cancelId(cancelId)
        .effectiveData(effectiveData)
        .expiryDate(expiryDate)
        .build();
  }

  public void updateStatusAsRedeem(String signUpId) {
    this.signUpId = signUpId;
  }

  public void updateStatusAsCancel(String cancelId) {
    this.cancelId = cancelId;
  }

  public void updateEventStatus(EventStatus eventStatus) {
    this.eventStatus = eventStatus;
  }

  public void setBeforeHistoryId(String historyId) {
    this.beforeHistoryId = historyId;
  }

  public boolean isStandby() {
    return eventStatus == STANDBY;
  }

  public boolean isQueueWait() {
    return eventStatus == EventStatus.QUEUE_WAIT;
  }

  public enum EventStatus {
    STANDBY,
    QUEUE_WAIT,
    QUEUE,
    PROGRESS,
    SUCCESS,
    FAILURE
  }
}


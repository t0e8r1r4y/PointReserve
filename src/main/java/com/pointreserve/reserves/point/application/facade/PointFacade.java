package com.pointreserve.reserves.point.application.facade;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.common.event.EventPublisher;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate;
import com.pointreserve.reserves.point.application.service.PointService;
import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.ui.dto.PointCancel;
import com.pointreserve.reserves.point.ui.dto.PointCreate;
import com.pointreserve.reserves.point.ui.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate.EventStatus.STANDBY;
import static java.lang.Math.abs;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointFacade {

  private final EventPublisher publisher;

  private final PointService pointService;

  private final AccumulatedPointService accumulatedPointService;

  @Transactional
  public PointResponse createEventReserves(PointCreate pointCreate) {

    Point point = pointCreate.toEntity();
    // 이벤트 저장
    Point saveResult = pointService.saveEventReserves(point);
    // 총계 업데이트
    accumulatedPointService.calcPointAndUpdate(saveResult.getMemberId(), saveResult.getAmount(),
        saveResult.getStatus());
    // 이벤트 발행
    PointDetailCreate pointDetailCreate = new PointDetailCreate(saveResult);
    pointDetailCreate.updateEventStatus(STANDBY);
    pointDetailCreate.setBeforeHistoryId(null);
    publisher.publish(pointDetailCreate);

    return PointResponse.builder()
        .point(saveResult)
        .build();
  }

  @Transactional
  public PointResponse createCancelEventReserves(PointCancel pointCancel) {

    Point point = pointCancel.toEntity();
    // 이벤트 저장
    Point saveResult = pointService.saveEventReserves(point);
    // 이전 정보 조회
    PointResponse beforeHistory = pointService.getEventReserves(pointCancel.getEventId());
    // 총계 업데이트
    accumulatedPointService.calcPointAndUpdate(saveResult.getMemberId(),
        abs(beforeHistory.getAmount()), saveResult.getStatus());
    // 이벤트 발행
    PointDetailCreate pointDetailCreate = new PointDetailCreate(saveResult);
    pointDetailCreate.setBeforeHistoryId(pointCancel.getEventId());
    pointDetailCreate.updateEventStatus(STANDBY);
    publisher.publish(pointDetailCreate);

    return PointResponse.builder()
        .point(saveResult)
        .build();
  }

}

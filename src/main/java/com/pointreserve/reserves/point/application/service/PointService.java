package com.pointreserve.reserves.point.application.service;

import com.pointreserve.reserves.common.event.EventPublisher;
import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.infra.PointRepository;
import com.pointreserve.reserves.point.exception.PointNotFoundException;
import com.pointreserve.reserves.point.ui.dto.PointResponse;
import com.pointreserve.reserves.point.ui.dto.PointSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

  private final EventPublisher publisher;
  private final PointRepository pointRepository;

  @Transactional
  public Point saveEventReserves(Point event) {
    return pointRepository.save(event);
  }


  @Transactional(readOnly = true)
  public PointResponse getEventReserves(String eventId) {
    Point point = pointRepository.findById(eventId).orElseThrow(
        PointNotFoundException::new
    );
    return PointResponse.builder().point(point).build();
  }

  @Transactional(readOnly = true)
  public List<PointResponse> getEventReservesList(PointSearch pointSearch) {
    List<PointResponse> responseList = pointRepository.getList(pointSearch)
        .stream().map(PointResponse::new)
        .collect(Collectors.toList());
    if (responseList.isEmpty()) {
      throw new PointNotFoundException();
    }
    return responseList;
  }
}
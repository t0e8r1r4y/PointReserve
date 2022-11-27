package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.pointdetail.application.service.PointDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventScheduler {

  private final EventDetailCreateQueue eventQueue;
  private final PointDetailService pointDetailService;

  @Scheduled(fixedRate = 100)
  public void schedule() {
    new EventDetailCreateWorker(eventQueue, pointDetailService)
        .run();
  }
}

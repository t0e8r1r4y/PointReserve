package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.common.event.EventDetailCreateQueue;
import com.pointreserve.reserves.common.event.EventDetailCreateWorker;
import com.pointreserve.reserves.eventdetail.application.service.EventDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventScheduler {
    private final EventDetailCreateQueue eventQueue;
    private final EventDetailService eventDetailService;

    // 제일 심플하게 여기 X
//    @Async("taskScheduler")
    @Scheduled(fixedRate = 100)
    public void schedule() {
        new EventDetailCreateWorker(eventQueue, eventDetailService)
                .run();
    }
}

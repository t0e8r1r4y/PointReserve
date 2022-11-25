package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDetailCreateListener {

    private final EventDetailCreateQueue eventDetailCreateQueue;

    @EventListener
    public void onEvent(PointDetailCreate pointDetailCreate){

        if(!pointDetailCreate.isStandby()) {
            log.info("EventDetail(id:{}) status is not STANDBY!", pointDetailCreate.getMembershipId());
            return;
        }

        while (eventDetailCreateQueue.isFull()) {
            if(!pointDetailCreate.isQueueWait()){
                pointDetailCreate.updateEventStatus(PointDetailCreate.EventStatus.QUEUE_WAIT);
            }
        }

        pointDetailCreate.updateEventStatus(PointDetailCreate.EventStatus.QUEUE);
        eventDetailCreateQueue.offer(pointDetailCreate);
    }
}

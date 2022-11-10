package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.common.event.EventDetailCreateQueue;
import com.pointreserve.reserves.eventdetail.ui.dto.EventDetailCreate;
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
    public void onEvent(EventDetailCreate eventDetailCreate){

        if(!eventDetailCreate.isStandby()) {
            log.info("EventDetail(id:{}) status is not STANDBY!", eventDetailCreate.getMembershipId());
            return;
        }

        while (eventDetailCreateQueue.isFull()) {
            if(!eventDetailCreate.isQueueWait()){
                eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.QUEUE_WAIT);
            }
        }

        eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.QUEUE);
        eventDetailCreateQueue.offer(eventDetailCreate);
    }
}

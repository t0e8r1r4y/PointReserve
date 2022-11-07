package com.pointreserve.reserves.common.service;

import com.pointreserve.reserves.common.event.EventDetailCreateQueue;
import com.pointreserve.reserves.eventDetail.application.service.EventDetailService;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class EventDetailCreateWorker implements Runnable{
    private final EventDetailCreateQueue eventQueue;
    private final EventDetailService eventDetailService;

    @Override
    @Transactional
    public void run() {
        if(eventQueue.isRemaining()){
            EventDetailCreate eventDetailCreate = eventQueue.poll();

            try{
                eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.PROGRESS);

                switch (eventDetailCreate.getStatus()){
                    case SAVEUP: {
                        eventDetailService.saveUpReserves(eventDetailCreate);
                        break;
                    }
                    case REDEEM: {
                        eventDetailService.redeemReserves(eventDetailCreate);
                        break;
                    }
                    case CANCLE_REDEEM: {
                        eventDetailService.cancelRedeemedBefore( eventDetailCreate.getBeforeHistoryId(), eventDetailCreate);
                        break;
                    }
                    default:
                        break;
                }

                eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.SUCCESS);
            } catch (Exception e) {
                eventDetailCreate.updateEventStatus(EventDetailCreate.EventStatus.FAILURE);
            }

        }
    }
}

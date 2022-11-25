package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.pointdetail.application.service.PointDetailService;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class EventDetailCreateWorker implements Runnable{
    private final EventDetailCreateQueue eventQueue;
    private final PointDetailService pointDetailService;

    @Override
    @Transactional
    public void run() {
        if(eventQueue.isRemaining()){
            PointDetailCreate pointDetailCreate = eventQueue.poll();

            try{
                pointDetailCreate.updateEventStatus(PointDetailCreate.EventStatus.PROGRESS);

                switch (pointDetailCreate.getStatus()){
                    case SAVEUP: {
                        pointDetailService.saveUpReserves(pointDetailCreate);
                        break;
                    }
                    case REDEEM: {
                        pointDetailService.redeemReserves(pointDetailCreate);
                        break;
                    }
                    case CANCLE_REDEEM: {
                        pointDetailService.cancelRedeemedBefore( pointDetailCreate.getBeforeHistoryId(), pointDetailCreate);
                        break;
                    }
                    default:
                        break;
                }

                pointDetailCreate.updateEventStatus(PointDetailCreate.EventStatus.SUCCESS);
            } catch (Exception e) {
                pointDetailCreate.updateEventStatus(PointDetailCreate.EventStatus.FAILURE);
            }

        }
    }
}

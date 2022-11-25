package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(PointDetailCreate pointDetailCreate)
    {
        publisher.publishEvent(pointDetailCreate);

        return;
    }
}

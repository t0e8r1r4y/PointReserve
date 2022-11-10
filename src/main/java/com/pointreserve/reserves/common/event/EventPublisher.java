package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.eventdetail.ui.dto.EventDetailCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(EventDetailCreate eventDetailCreate)
    {
        publisher.publishEvent(eventDetailCreate);

        return;
    }
}

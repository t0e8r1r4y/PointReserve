package com.pointreserve.reserves.eventreserves.application.service;

import com.pointreserve.reserves.common.event.EventPublisher;
import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.infra.EventReservesRepository;
import com.pointreserve.reserves.eventreserves.exception.EventReservesNotFoundException;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesResponse;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventReservesService {
    private final EventPublisher publisher;
    private final EventReservesRepository eventReservesRepository;

    @Transactional
    public EventReserves saveEventReserves(EventReserves e) {
        return eventReservesRepository.save(e);
    }


    @Transactional(readOnly = true)
    public EventReservesResponse getEventReserves(String eventId){
        EventReserves eventReserves = eventReservesRepository.findById(eventId).orElseThrow(
                EventReservesNotFoundException::new
        );
        return EventReservesResponse.builder().eventReserves(eventReserves).build();
    }

    @Transactional(readOnly = true)
    public List<EventReservesResponse> getEventReservesList(EventReservesSearch eventReservesSearch){
        List<EventReservesResponse> responseList = eventReservesRepository.getList(eventReservesSearch)
                .stream().map(EventReservesResponse::new)
                .collect(Collectors.toList());
        if(responseList.isEmpty()){
            throw new EventReservesNotFoundException();
        }
        return responseList;
    }
}
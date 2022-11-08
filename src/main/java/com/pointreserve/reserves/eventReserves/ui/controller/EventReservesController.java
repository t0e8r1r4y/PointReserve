package com.pointreserve.reserves.eventReserves.ui.controller;

import com.pointreserve.reserves.eventReserves.application.facade.EventReservesFacade;
import com.pointreserve.reserves.eventReserves.application.service.EventReservesService;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCancel;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCreate;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesResponse;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventReservesController {

    private final EventReservesService eventReservesService;

    private final EventReservesFacade eventReservesFacade;

    @GetMapping("/reserves/events/get/{eventId}")
    public EventReservesResponse getEvent(@PathVariable(name = "eventId") String eventId){
        return eventReservesService.getEventReserves(eventId);
    }

    @GetMapping("/reserves/events/getList")
    public List<EventReservesResponse> getEventList(@ModelAttribute EventReservesSearch params){
        return eventReservesService.getEventReservesList(params);
    }

    @PostMapping("/reserves/event/create")
    public EventReservesResponse createEvent(@RequestBody EventReservesCreate params) {
        params.isAmountValid();
        params.isStatusValid();
//        return eventReservesService.createEventReserves(params);
        return eventReservesFacade.createEventReserves(params);
    }

    @PostMapping("/reserves/event/cancel")
    public EventReservesResponse createCacelEvent(@RequestBody EventReservesCancel params) {
        params.isStatusValid();
//        return eventReservesService.createCancelEventReserves(params);
        return eventReservesFacade.createCancelEventReserves(params);
    }

}
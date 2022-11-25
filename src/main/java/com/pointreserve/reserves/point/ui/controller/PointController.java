package com.pointreserve.reserves.point.ui.controller;

import com.pointreserve.reserves.point.application.facade.PointFacade;
import com.pointreserve.reserves.point.application.service.PointService;
import com.pointreserve.reserves.point.ui.dto.PointCancel;
import com.pointreserve.reserves.point.ui.dto.PointCreate;
import com.pointreserve.reserves.point.ui.dto.PointResponse;
import com.pointreserve.reserves.point.ui.dto.PointSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    private final PointFacade pointFacade;

    @GetMapping("/reserves/events/get/{eventId}")
    public PointResponse getEvent(@PathVariable(name = "eventId") String eventId){
        return pointService.getEventReserves(eventId);
    }

    @GetMapping("/reserves/events/getList")
    public List<PointResponse> getEventList(@ModelAttribute PointSearch params){
        return pointService.getEventReservesList(params);
    }

    @PostMapping("/reserves/event/create")
    public PointResponse createEvent(@RequestBody PointCreate params) {
        params.isAmountValid();
        params.isStatusValid();
        return pointFacade.createEventReserves(params);
    }

    @PostMapping("/reserves/event/cancel")
    public PointResponse createCacelEvent(@RequestBody PointCancel params) {
        params.isStatusValid();
        return pointFacade.createCancelEventReserves(params);
    }

}
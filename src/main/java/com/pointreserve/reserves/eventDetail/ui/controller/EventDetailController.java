package com.pointreserve.reserves.eventDetail.ui.controller;

import com.pointreserve.reserves.eventDetail.application.service.EventDetailService;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailResponse;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventDetailController {

    private final EventDetailService eventDetailService;

    @GetMapping("/reserves/eventsDetail/getList")
    public List<EventDetailResponse> getEventList(@ModelAttribute EventDetailSearch params){
        return eventDetailService.getEventDetailList(params);
    }
}

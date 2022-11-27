package com.pointreserve.reserves.pointdetail.ui.controller;

import com.pointreserve.reserves.pointdetail.application.service.PointDetailService;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailResponse;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointDetailController {

  private final PointDetailService pointDetailService;

  @GetMapping("/reserves/eventsDetail/getList")
  public List<PointDetailResponse> getEventList(@ModelAttribute PointDetailSearch params) {
    return pointDetailService.getEventDetailList(params);
  }
}

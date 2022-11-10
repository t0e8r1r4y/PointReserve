package com.pointreserve.reserves.eventReserves.application.facade;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointEdit;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.eventReserves.application.service.EventReservesService;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCreate;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static com.pointreserve.reserves.eventReserves.domain.ReservesStatus.*;

@SpringBootTest
@ActiveProfiles("local")
class EventReservesFacadeTest {

    @Autowired
    private EventReservesFacade eventReservesFacade;

    @MockBean
    private AccumulatedPointService accumulatedPointService;

    @SpyBean
    private EventReservesService eventReservesService;


//    @Test
//    void createEventReserves() {
//        // given
//        EventReservesCreate given = EventReservesCreate.builder().memberId(1L).amount(10).reservesStatus(SAVEUP).build();
//
//        Mockito.when( accumulatedPointService.getAccumulatedPoint( given.getMemberId() ) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 100);
//        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(given.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 110);
//        });
//
//
//        // when
//        EventReservesResponse response = eventReservesFacade.createEventReserves(given);
//
//        // then
//        System.out.println(response.getId());
//        System.out.println(response.getAmount());
//        System.out.println(response.getMemberId());
//        System.out.println(response.getStatus());
//        System.out.println(response.getEffectiveData());
//        System.out.println(response.getExpiryDate());
//        Assertions.assertEquals(1L , response.getMemberId());
//        Assertions.assertEquals(10 , response.getAmount());
//        Assertions.assertEquals(SAVEUP , response.getStatus());
//    }
}
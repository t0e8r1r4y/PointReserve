package com.pointreserve.reserves.eventreserves.application.facade;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.eventreserves.application.service.EventReservesService;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static com.pointreserve.reserves.eventreserves.domain.ReservesStatus.*;

@SpringBootTest
@ActiveProfiles("local")
class EventReservesFacadeTest {

    @Autowired
    private EventReservesFacade eventReservesFacade;

    @SpyBean
    private AccumulatedPointService accumulatedPointService;

    @SpyBean
    private EventReservesService eventReservesService;


    @Test
    void createEventReserves() {
        // given
        EventReservesCreate given = EventReservesCreate.builder().memberId(1L).amount(10).reservesStatus(SAVEUP).build();

        // then
        Assertions.assertThrows( AccumulatedPointNotFoundException.class, () -> {
            eventReservesFacade.createEventReserves(given);
        });
    }
}
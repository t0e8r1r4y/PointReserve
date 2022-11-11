package com.pointreserve.reserves.eventreserves.application.facade;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointFactoryImpl;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
import com.pointreserve.reserves.eventreserves.application.service.EventReservesService;
import com.pointreserve.reserves.eventreserves.infra.EventReservesRepository;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static com.pointreserve.reserves.eventreserves.domain.ReservesStatus.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("local")
class EventReservesFacadeTest {

    @Autowired
    private EventReservesFacade eventReservesFacade;

    @SpyBean
    private AccumulatedPointService accumulatedPointService;

    @Autowired
    private EventReservesRepository eventReservesRepository;

    @Autowired
    private AccumulatedPointPointRepository accumulatedPointPointRepository;

    @SpyBean
    private EventReservesService eventReservesService;

    private AccumulatedPointFactoryImpl factory = new AccumulatedPointFactoryImpl();

    @BeforeEach
    void clearRepo(){
        eventReservesRepository.deleteAll();
        accumulatedPointPointRepository.deleteAll();
    }

    @Test
    void createEventReservesFailByNotFoundException() {
        // given
        EventReservesCreate given = EventReservesCreate.builder().memberId(1L).amount(10).reservesStatus(SAVEUP).build();
        // then
        Assertions.assertThrows( AccumulatedPointNotFoundException.class, () -> {
            eventReservesFacade.createEventReserves(given);
        });
        Assertions.assertEquals(0,eventReservesRepository.findAll().size());
    }

    @Test
    void createEventReservesFailByInvalidValue() {
        // given
        accumulatedPointPointRepository.save(factory.createAccumulatedPoint(1L, 0));
        EventReservesCreate given = EventReservesCreate.builder().memberId(1L).amount(10).reservesStatus(REDEEM).build();
        // then
        Assertions.assertThrows( AccumulatedPointInvalidRequestException.class, () -> {
            eventReservesFacade.createEventReserves(given);
        });
        Assertions.assertEquals(0,eventReservesRepository.findAll().size());
    }
}
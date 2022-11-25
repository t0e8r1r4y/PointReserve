package com.pointreserve.reserves.point.application.facade;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointFactoryImpl;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
import com.pointreserve.reserves.point.application.service.PointService;
import com.pointreserve.reserves.point.infra.PointRepository;
import com.pointreserve.reserves.point.ui.dto.PointCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static com.pointreserve.reserves.point.domain.PointStatus.*;

@SpringBootTest
@ActiveProfiles("local")
class PointFacadeTest {

    @Autowired
    private PointFacade pointFacade;

    @SpyBean
    private AccumulatedPointService accumulatedPointService;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private AccumulatedPointPointRepository accumulatedPointPointRepository;

    @SpyBean
    private PointService pointService;

    private AccumulatedPointFactoryImpl factory = new AccumulatedPointFactoryImpl();

    @BeforeEach
    void clearRepo(){
        pointRepository.deleteAll();
        accumulatedPointPointRepository.deleteAll();
    }

    @Test
    void createEventReservesFailByNotFoundException() {
        // given
        PointCreate given = PointCreate.builder().memberId(1L).amount(10).pointStatus(SAVEUP).build();
        // then
        Assertions.assertThrows( AccumulatedPointNotFoundException.class, () -> {
            pointFacade.createEventReserves(given);
        });
        Assertions.assertEquals(0, pointRepository.findAll().size());
    }

    @Test
    void createEventReservesFailByInvalidValue() {
        // given
        accumulatedPointPointRepository.save(factory.createAccumulatedPoint(1L, 0));
        PointCreate given = PointCreate.builder().memberId(1L).amount(10).pointStatus(REDEEM).build();
        // then
        Assertions.assertThrows( AccumulatedPointInvalidRequestException.class, () -> {
            pointFacade.createEventReserves(given);
        });
        Assertions.assertEquals(0, pointRepository.findAll().size());
    }
}
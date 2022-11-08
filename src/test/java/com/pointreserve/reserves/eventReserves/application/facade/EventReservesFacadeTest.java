package com.pointreserve.reserves.eventReserves.application.facade;

import com.pointreserve.reserves.account.application.service.AccountService;
import com.pointreserve.reserves.common.component.EventPublisher;
import com.pointreserve.reserves.eventReserves.application.service.EventReservesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventReservesFacadeTest {

    @Autowired
    private EventReservesFacade eventReservesFacade;

    @MockBean
    private AccountService accountService;

    @MockBean
    private EventReservesService eventReservesService;

    @MockBean
    private EventPublisher publisher;

    @Test
    void createEventReserves() {
    }

    @Test
    void createCancelEventReserves() {
    }
}
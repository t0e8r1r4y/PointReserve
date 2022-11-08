package com.pointreserve.reserves.eventReserves.application.facade;

import com.pointreserve.reserves.account.application.service.AccountService;
import com.pointreserve.reserves.account.domain.Account;
import com.pointreserve.reserves.account.exception.AccountNotFound;
import com.pointreserve.reserves.account.ui.dto.AccountEdit;
import com.pointreserve.reserves.account.ui.dto.AccountResponse;
import com.pointreserve.reserves.common.component.EventPublisher;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailCreate;
import com.pointreserve.reserves.eventReserves.application.service.EventReservesService;
import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCreate;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.pointreserve.reserves.eventReserves.domain.ReservesStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventReservesFacadeTest {

    @Autowired
    private EventReservesFacade eventReservesFacade;

    @MockBean
    private AccountService accountService;

    @SpyBean
    private EventReservesService eventReservesService;


    @Test
    void createEventReserves() {
        // given
        EventReservesCreate given = EventReservesCreate.builder().memberId(1L).amount(10).reservesStatus(SAVEUP).build();

        Mockito.when( accountService.getAccount( given.getMemberId() ) ).then(invocation -> {
            return new AccountResponse(1L, 1L, 100);
        });
        Mockito.when( accountService.updateAccount(given.getMemberId(), AccountEdit.builder().build()) ).then(invocation -> {
            return new AccountResponse(1L, 1L, 110);
        });


        // when
        EventReservesResponse response = eventReservesFacade.createEventReserves(given);

        // then
        System.out.println(response.getId());
        System.out.println(response.getAmount());
        System.out.println(response.getMemberId());
        System.out.println(response.getStatus());
        System.out.println(response.getEffectiveData());
        System.out.println(response.getExpiryDate());
        Assertions.assertEquals(1L , response.getMemberId());
        Assertions.assertEquals(10 , response.getAmount());
        Assertions.assertEquals(SAVEUP , response.getStatus());
    }
}
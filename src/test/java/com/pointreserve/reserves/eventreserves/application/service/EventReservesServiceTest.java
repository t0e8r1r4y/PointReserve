package com.pointreserve.reserves.eventreserves.application.service;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.infra.EventReservesRepository;
import com.pointreserve.reserves.eventreserves.exception.EventReservesNotFoundException;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesResponse;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static com.pointreserve.reserves.eventreserves.domain.ReservesStatus.SAVEUP;

@SpringBootTest
@ActiveProfiles("local")
class EventReservesServiceTest {

    @Autowired
    private EventReservesService eventReservesService;

    @Autowired
    private EventReservesRepository eventReservesRepository;

    @MockBean
    private AccumulatedPointService accumulatedPointService;

    @BeforeEach
    void clean() {
        eventReservesRepository.deleteAll();
    }


    @Test
    @DisplayName("단건 이벤트 조회 서비스 테스트")
    void getEventReservesTest() {
        // given
        EventReserves given = EventReserves.builder().memberId(1L).amount(10).status(SAVEUP).build();
        EventReserves saveResult = eventReservesRepository.save(given);

        // when
        EventReservesResponse response = eventReservesService.getEventReserves(saveResult.getId());

        // then
        Assertions.assertEquals(given.getMemberId(), response.getMemberId());
        Assertions.assertEquals(given.getAmount(), response.getAmount());
        Assertions.assertEquals(given.getStatus(), response.getStatus());
    }

    @Test
    @DisplayName("단건 이벤트 조회 실패 서비스 테스트")
    void getEventReservesFailTest() {
        // expect
        Assertions.assertThrows( EventReservesNotFoundException.class, () -> {
            eventReservesService.getEventReserves("e-1");
        });
    }

    @Test
    @DisplayName("다수 이벤트 조회 테스트")
    void getEventReservesListTest() {
        // given
        List<EventReserves> givenList = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            givenList.add(EventReserves.builder().memberId(1L).amount(i+1).status(SAVEUP).build());
        }
        List<EventReserves> saveResult = eventReservesRepository.saveAllAndFlush(givenList);

        // when
        List<EventReservesResponse> responseList = eventReservesService.getEventReservesList(EventReservesSearch.builder()
                        .memberId(1L)
                        .page(0)
                        .size(10)
                .build());

        System.out.println(responseList.size());

        // then
        Assertions.assertEquals(responseList.size(), 10);
        for(int i = 0; i < responseList.size(); i++) {
            System.out.println(responseList.get(i).getMemberId() + " " + responseList.get(i).getAmount() + " " + responseList.get(i).getStatus());
            Assertions.assertEquals(responseList.get(i).getMemberId(), 1L);
            Assertions.assertEquals(responseList.get(i).getAmount(), saveResult.get(i).getAmount());
            Assertions.assertEquals(responseList.get(i).getStatus(), saveResult.get(i).getStatus());
        }
    }

}

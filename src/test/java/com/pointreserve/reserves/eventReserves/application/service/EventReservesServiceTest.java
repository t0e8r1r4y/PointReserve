package com.pointreserve.reserves.eventReserves.application.service;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointInvalidRequestException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointEdit;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.infra.EventReservesRepository;
import com.pointreserve.reserves.eventReserves.exception.EventReservesNotFound;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesCreate;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesResponse;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static com.pointreserve.reserves.eventReserves.domain.ReservesStatus.SAVEUP;

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
    @DisplayName("이벤트 등록 서비스 테스트")
    void createEventReservesTest() {
        // given
        EventReservesCreate given = EventReservesCreate.builder().memberId(1L).amount(10).reservesStatus(SAVEUP).build();

        Mockito.when( accumulatedPointService.getAccumulatedPoint( given.getMemberId() ) ).then(invocation -> {
            return new AccumulatedPointResponse(1L, 1L, 100);
        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(given.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 110);
//        });

        // when
        eventReservesService.createEventReserves(given);

        // then
        EventReserves response = eventReservesRepository.findAll().get(0);
        Assertions.assertEquals(1, eventReservesRepository.count());
        Assertions.assertEquals(1L , response.getMemberId());
        Assertions.assertEquals(10 , response.getAmount());
        Assertions.assertEquals(SAVEUP , response.getStatus());
    }

//    @Test
//    @DisplayName("이벤트 등록 실패 서비스 테스트. 생성 요청데이터의 memberId가 null이거나 amount가 0이하이면 SQL 쿼리가 실행되지 않으며 에러를 리턴한다.")
//    void createEventReservesFailTest() {
//        // given
//        EventReservesCreate given = EventReservesCreate.builder().memberId(null).amount(0).reservesStatus(SAVEUP).build();
//
//        Mockito.when( accumulatedPointService.getAccumulatedPoint( given.getMemberId() ) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 100);
//        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(given.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 110);
//        });
//
//        // then
//        Assertions.assertThrows( DataIntegrityViolationException.class, () -> {
//            eventReservesService.createEventReserves(given);
//        });
//    }
//
//    @Test
//    @DisplayName("이벤트 등록 실패 서비스 테스트. 이벤트 등록과정에서 전체 금액을 업데이트 해야되는데 전체 업데이트 금액이 마이너스 일때 에러를 리턴한다.")
//    void createEventReservesFail2Test() {
//        // given
//        EventReservesCreate given = EventReservesCreate.builder().memberId(null).amount(0).reservesStatus(SAVEUP).build();
//
//        Mockito.when( accumulatedPointService.getAccumulatedPoint( given.getMemberId() ) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, -10);
//        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(given.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return AccumulatedPointResponse.builder().build();
//        });
//
//        // then
//        Assertions.assertThrows( AccumulatedPointInvalidRequestException.class, () -> {
//            eventReservesService.createEventReserves(given);
//        });
//    }

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
        Assertions.assertThrows( EventReservesNotFound.class, () -> {
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

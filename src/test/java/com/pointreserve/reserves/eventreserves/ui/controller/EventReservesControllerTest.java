package com.pointreserve.reserves.eventreserves.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.infra.EventReservesRepository;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesCancel;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.pointreserve.reserves.eventreserves.domain.ReservesStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.PointReserve.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("local")
class EventReservesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventReservesRepository eventReservesRepository;

    @Autowired
    private AccumulatedPointPointRepository accumulatedPointPointRepository;

    @MockBean
    private AccumulatedPointService accumulatedPointService;

    @BeforeEach
    void clean() {
        eventReservesRepository.deleteAll();
        accumulatedPointPointRepository.deleteAll();
    }

    @Test
    @DisplayName("Event를 생성하는 테스트입니다.")
    void createEventTest() throws Exception {

        EventReservesCreate eventReservesCreate = EventReservesCreate.builder()
                .amount(10)
                .memberId(1L)
                .reservesStatus(REDEEM).build();

        Mockito.when( accumulatedPointService.getAccumulatedPoint( eventReservesCreate.getMemberId() ) ).then(invocation -> {
            return new AccumulatedPointResponse(1L, 1L, 100);
        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(eventReservesCreate.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 110);
//        });


        ObjectMapper objectMapper = new ObjectMapper();
        String given = objectMapper.writeValueAsString(eventReservesCreate);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/reserves/event/create")
                        .contentType(APPLICATION_JSON)
                        .content(given))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(String.valueOf(1L)))
                .andDo(document(
                    "create_event",
                        requestFields(
                                attributes(key("Body").value("")),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("amount").description("적립 금액 혹은 사용 금액입니다."),
                                fieldWithPath("reservesStatus").description("적립, 사용 상태를 나타냅니다.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포인트 이벤트 ID"),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("amount").description("적립 금액 혹은 사용 금액입니다."),
                                fieldWithPath("status").description("적립, 사용 상태를 나타냅니다."),
                                fieldWithPath("effectiveData").description("포인트 이벤트가 발생한 시점으로 적립금의 효력이 시작되는 날짜입니다."),
                                fieldWithPath("expiryDate").description("포인트의 효력이 상실되는 만료일입니다. 시작일 +1년")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("Event를 생성하는 테스트입니다. 금액이 0원 이하일 경우 에러를 반환합니다.")
    void createEventFailTest() throws Exception {

        EventReservesCreate eventReservesCreate = EventReservesCreate.builder()
                .amount(0)
                .memberId(1L)
                .reservesStatus(REDEEM).build();

        Mockito.when( accumulatedPointService.getAccumulatedPoint( eventReservesCreate.getMemberId() ) ).then(invocation -> {
            return new AccumulatedPointResponse(1L, 1L, 100);
        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(eventReservesCreate.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 110);
//        });


        ObjectMapper objectMapper = new ObjectMapper();
        String given = objectMapper.writeValueAsString(eventReservesCreate);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/reserves/event/create")
                        .contentType(APPLICATION_JSON)
                        .content(given))
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "create_event_fail",
                        requestFields(
                                attributes(key("Body").value("")),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("amount").description("적립 금액 혹은 사용 금액입니다."),
                                fieldWithPath("reservesStatus").description("적립, 사용 상태를 나타냅니다.")
                        ),
                        responseFields(
                                fieldWithPath("code").description("400을 리턴합니다."),
                                fieldWithPath("message").description("'잘못된 요청입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.amount").description("'금액은 0원 이하가 될 수 없습니다.' 문구를 반환합니다.")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("Event를 생성하는 테스트입니다. 금액이 1,000,000원 초과일 경우 에러를 반환합니다.")
    void createEventFail2Test() throws Exception {

        EventReservesCreate eventReservesCreate = EventReservesCreate.builder()
                .amount(10000000)
                .memberId(1L)
                .reservesStatus(REDEEM).build();

        Mockito.when( accumulatedPointService.getAccumulatedPoint( eventReservesCreate.getMemberId() ) ).then(invocation -> {
            return new AccumulatedPointResponse(1L, 1L, 100);
        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(eventReservesCreate.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 110);
//        });


        ObjectMapper objectMapper = new ObjectMapper();
        String given = objectMapper.writeValueAsString(eventReservesCreate);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/reserves/event/create")
                        .contentType(APPLICATION_JSON)
                        .content(given))
                .andExpect(status().isBadRequest())
                .andDo(document(
                        "create_event_fail2",
                        requestFields(
                                attributes(key("Body").value("")),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("amount").description("적립 금액 혹은 사용 금액입니다."),
                                fieldWithPath("reservesStatus").description("적립, 사용 상태를 나타냅니다.")
                        ),
                        responseFields(
                                fieldWithPath("code").description("400을 리턴합니다."),
                                fieldWithPath("message").description("'잘못된 요청입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.amount").description("'적립금액은 1,000,000원을 초과 할 수 없습니다.' 문구를 반환합니다.")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("Event를 단건 조회하는 테스트입니다.")
    void getEventTest() throws Exception {
        EventReserves eventReserves = eventReservesRepository.save(EventReserves.builder()
                .memberId(1L)
                .amount(10)
                .status(SAVEUP)
                .build());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/reserves/events/get/{eventId}", eventReserves.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(String.valueOf(1L)))
                .andExpect(jsonPath("$.amount").value(String.valueOf(10)))
                .andExpect(jsonPath("$.status").value("SAVEUP"))
                .andDo(document(
                        "get_event_one",
                        pathParameters(
                                parameterWithName("eventId").description("적립금 이벤트 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포인트 이벤트 ID"),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("amount").description("적립 금액 혹은 사용 금액입니다. 취소금액은 0입니다."),
                                fieldWithPath("status").description("적립, 사용, 적립 취소 상태를 나타냅니다."),
                                fieldWithPath("effectiveData").description("포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다."),
                                fieldWithPath("expiryDate").description("포인트의 효력이 상실되는 만료일입니다. 시작일 +1년")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("Event를 단건 조회를 실패하는 테스트입니다.")
    void getEventFailTest() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/reserves/events/get/{eventId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document(
                        "get_event_one_fail",
                        pathParameters(
                                parameterWithName("eventId").description("적립금 이벤트 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("404를 반환합니다."),
                                fieldWithPath("message").description("'존재하지 않는 Event입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.errorResponse").description("위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.")
                        )
                ))
                .andDo(print());
    }


    @Test
    @DisplayName("Event를 페이징 처리하여 조회하는 테스트입니다. 조회된 결과가 없는경우 Not Found 에러를 반환합니다.")
    void getEventListTest() throws Exception {

        // expect
        mockMvc.perform(RestDocumentationRequestBuilders.get("/reserves/events/getList?page=2&size=10&memberId=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document(
                        "get_event_list_fail",
                        requestParameters(
                                parameterWithName("page").description("조회하고자 하는 페이지 번호"),
                                parameterWithName("size").description("한 페이지에 조회할 개수"),
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("404를 반환합니다."),
                                fieldWithPath("message").description("'존재하지 않는 Event입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.errorResponse").description("위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("Event를 페이징 처리하여 조회하는 테스트입니다.")
    void getEventListFailTest() throws Exception {
        // given -> None
        eventReservesRepository.saveAll(makeTestList());
        // expect
        mockMvc.perform(RestDocumentationRequestBuilders.get("/reserves/events/getList?page=2&size=10&memberId=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberId").value(1L))
                .andExpect(jsonPath("$.length()").value(10))
                .andDo(document(
                        "get_event_list",
                        requestParameters(
                                parameterWithName("page").description("조회하고자 하는 페이지 번호"),
                                parameterWithName("size").description("한 페이지에 조회할 개수"),
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("포인트 이벤트 상세 ID"),
                                fieldWithPath("[].memberId").description("회원번호"),
                                fieldWithPath("[].status").description("적립, 사용, 적립 취소 상태를 나타냅니다."),
                                fieldWithPath("[].amount").description("적립 금액 혹은 사용 금액입니다."),
                                fieldWithPath("[].effectiveData").description("포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다."),
                                fieldWithPath("[].expiryDate").description("포인트의 효력이 상실되는 만료일입니다. 시작일 +1년 ")
                        )
                ))
                .andDo(print());
    }

    private List<EventReserves> makeTestList(){
        List<EventReserves> givenList = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            givenList.add(EventReserves.builder().memberId(1L).amount(i+1).status(SAVEUP).build());
        }
        return givenList;
    }

    @Test
    @DisplayName("적립 취소 Event를 생성하는 테스트입니다.")
    void createCacelEventTest() throws Exception {
        // given
        EventReserves saveUpEvent = EventReserves.builder()
                .amount(50)
                .memberId(1L)
                .status(SAVEUP)
                .build();
        eventReservesRepository.save(saveUpEvent);

        EventReserves redeemEvent = EventReserves.builder()
                .amount(50)
                .memberId(1L)
                .status(REDEEM)
                .build();
        eventReservesRepository.save(redeemEvent);

        EventReservesCancel eventReservesCancel = EventReservesCancel.builder()
                .eventId("e-2")
                .memberId(1L)
                .reservesStatus(CANCLE_REDEEM)
                .build();

        Mockito.when( accumulatedPointService.getAccumulatedPoint( eventReservesCancel.getMemberId() ) ).then(invocation -> {
            return new AccumulatedPointResponse(1L, 1L, 100);
        });
//        Mockito.when( accumulatedPointService.updateAccumulatedPoint(eventReservesCancel.getMemberId(), AccumulatedPointEdit.builder().build()) ).then(invocation -> {
//            return new AccumulatedPointResponse(1L, 1L, 110);
//        });

        ObjectMapper objectMapper = new ObjectMapper();
        String given = objectMapper.writeValueAsString(eventReservesCancel);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/reserves/event/cancel")
                        .contentType(APPLICATION_JSON)
                        .content(given))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(String.valueOf(1L)))
                .andDo(document(
                        "create_cacel_event",
                        requestFields(
                                attributes(key("Body").value("")),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("eventId").description("적립 사용을 취소할 ID입니다."),
                                fieldWithPath("reservesStatus").description("사용 취소 상태를 나타냅니다.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포인트 이벤트 ID"),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("amount").description("사용 취소의 경우 0원을 표시합니다."),
                                fieldWithPath("status").description("사용 취소 상태를 나타냅니다."),
                                fieldWithPath("effectiveData").description("취소된 포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다."),
                                fieldWithPath("expiryDate").description("취소된 포인트의 효력이 상실되는 만료일입니다. 시작일 +1년")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("적립 취소 Event를 생성하는 테스트입니다. 취소 대상인 이벤트가 없어 실패합니다.")
    void createCacelEventFailTest() throws Exception {
        // given
        EventReservesCancel eventReservesCancel = EventReservesCancel.builder()
                .eventId("e-2")
                .memberId(2L)
                .reservesStatus(CANCLE_REDEEM)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String given = objectMapper.writeValueAsString(eventReservesCancel);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/reserves/event/cancel")
                        .contentType(APPLICATION_JSON)
                        .content(given))
                .andExpect(status().isNotFound())
                .andDo(document(
                        "create_cacel_event_fail",
                        requestFields(
                                attributes(key("Body").value("")),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("eventId").description("적립 사용을 취소할 ID입니다."),
                                fieldWithPath("reservesStatus").description("사용 취소 상태를 나타냅니다.")
                        ),
                        responseFields(
                                fieldWithPath("code").description("404를 반환합니다."),
                                fieldWithPath("message").description("'존재하지 않는 Event입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.errorResponse").description("위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.")
                        )
                ))
                .andDo(print());
    }
}

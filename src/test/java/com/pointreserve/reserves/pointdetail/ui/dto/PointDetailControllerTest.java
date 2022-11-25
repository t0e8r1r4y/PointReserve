package com.pointreserve.reserves.pointdetail.ui.dto;

import com.pointreserve.reserves.pointdetail.infra.PointDetailRepository;
import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.infra.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.pointreserve.reserves.point.domain.PointStatus.SAVEUP;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.PointReserve.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("local")
class PointDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailRepository pointDetailRepository;

    @BeforeEach
    void clean(){
        pointDetailRepository.deleteAll();
        pointRepository.deleteAll();
    }

    @Test
    @DisplayName("Event를 페이징 처리하여 조회하는 테스트입니다.")
    void getEventListTest() throws Exception {
        // given
        makeEvetDetailForRedeem();

        mockMvc.perform(get("/reserves/eventsDetail/getList?page=1&size=10&memberId=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].membershipId").value(1L))
                .andExpect(jsonPath("$.length()").value(10))
                .andDo(document(
                        "get_event_Detail_list",
                        requestParameters(
                                parameterWithName("page").description("조회하고자 하는 페이지 번호"),
                                parameterWithName("size").description("한 페이지에 조회할 개수"),
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                            fieldWithPath("[].id").description("포인트 이벤트 상세 ID"),
                            fieldWithPath("[].membershipId").description("회원번호"),
                            fieldWithPath("[].status").description("적립, 사용, 적립 취소 상태를 나타냅니다."),
                            fieldWithPath("[].amount").description("적립 금액 혹은 사용 금액입니다."),
                            fieldWithPath("[].eventId").description("해당 이벤트 상세를 포함하는 이벤트의 id입니다."),
                            fieldWithPath("[].signUpId").description("포인트 사용 시, 적립 이벤트 상세의 id입니다."),
                            fieldWithPath("[].cancelId").description("포인트 사용 취소시, 사용 이벤트 상세의 id입니다."),
                            fieldWithPath("[].effectiveData").description("포인트 이벤트가 발생한 시점으로 포인트의 효력이 시작되는 날짜입니다."),
                            fieldWithPath("[].expiryDate").description("포인트의 효력이 상실되는 만료일입니다. 시작일 +1년 ")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("Event를 페이징 처리하여 조회하는 테스트입니다. 발견한 데이터가 없어서 실패하는 경우입니다.")
    void getEventListFailTest() throws Exception {
        // given

        // expect
        mockMvc.perform(get("/reserves/eventsDetail/getList?page=1&size=10&memberId=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document(
                        "get_event_Detail_list_fail",
                        requestParameters(
                                parameterWithName("page").description("조회하고자 하는 페이지 번호"),
                                parameterWithName("size").description("한 페이지에 조회할 개수"),
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("404를 반환합니다."),
                                fieldWithPath("message").description("'존재하지 않는 상세정보입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.errorResponse").description("위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.")
                        )
                ))
                .andDo(print());
    }

    private void makeEvetDetailForRedeem(){
        Long memberId = 1L;
        for(int i = 0; i < 30; i++) {
            Point e = pointRepository.save(Point.builder().memberId(memberId).amount(i+1).status(SAVEUP).build());
            pointDetailRepository.save(new PointDetailCreate(e).toEntity());
        }
        pointDetailRepository.findAll();
    }

}
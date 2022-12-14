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
    @DisplayName("Event??? ????????? ???????????? ???????????? ??????????????????.")
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
                                parameterWithName("page").description("??????????????? ?????? ????????? ??????"),
                                parameterWithName("size").description("??? ???????????? ????????? ??????"),
                                parameterWithName("memberId").description("????????????")
                        ),
                        responseFields(
                            fieldWithPath("[].id").description("????????? ????????? ?????? ID"),
                            fieldWithPath("[].membershipId").description("????????????"),
                            fieldWithPath("[].status").description("??????, ??????, ?????? ?????? ????????? ???????????????."),
                            fieldWithPath("[].amount").description("?????? ?????? ?????? ?????? ???????????????."),
                            fieldWithPath("[].eventId").description("?????? ????????? ????????? ???????????? ???????????? id?????????."),
                            fieldWithPath("[].signUpId").description("????????? ?????? ???, ?????? ????????? ????????? id?????????."),
                            fieldWithPath("[].cancelId").description("????????? ?????? ?????????, ?????? ????????? ????????? id?????????."),
                            fieldWithPath("[].effectiveData").description("????????? ???????????? ????????? ???????????? ???????????? ????????? ???????????? ???????????????."),
                            fieldWithPath("[].expiryDate").description("???????????? ????????? ???????????? ??????????????????. ????????? +1??? ")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("Event??? ????????? ???????????? ???????????? ??????????????????. ????????? ???????????? ????????? ???????????? ???????????????.")
    void getEventListFailTest() throws Exception {
        // given

        // expect
        mockMvc.perform(get("/reserves/eventsDetail/getList?page=1&size=10&memberId=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document(
                        "get_event_Detail_list_fail",
                        requestParameters(
                                parameterWithName("page").description("??????????????? ?????? ????????? ??????"),
                                parameterWithName("size").description("??? ???????????? ????????? ??????"),
                                parameterWithName("memberId").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("404??? ???????????????."),
                                fieldWithPath("message").description("'???????????? ?????? ?????????????????????.' ????????? ???????????????."),
                                fieldWithPath("validation.errorResponse").description("??? message??? ????????? ???????????????. ?????? ????????? ?????? ????????? ?????? ?????? ?????? ????????? ???????????????.")
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
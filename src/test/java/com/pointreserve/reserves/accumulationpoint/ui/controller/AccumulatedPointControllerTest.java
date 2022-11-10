package com.pointreserve.reserves.accumulationpoint.ui.controller;

import com.pointreserve.reserves.accumulationpoint.application.service.AccumulatedPointService;
import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.PointReserve.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("local")
class AccumulatedPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccumulatedPointService accumulatedPointService;

    @Autowired
    private AccumulatedPointPointRepository accumulatedPointPointRepository;

    @BeforeEach
    void clean(){
        accumulatedPointPointRepository.deleteAll();
        accumulatedPointService.clearBucket();
    }

    @Test
    @DisplayName("포인트를 관리하기 위한 포인트 계정을 생성하는 테스트입니다. (createAccount 메서드 테스트)")
    void createAmountTest() throws Exception {
        // given
        Long memberId = 1L;
        mockMvc.perform(post("/reserves/create/{memberId}", memberId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.totalAmount").value(0))
                .andDo(print())
                .andDo(document(
                        "account_create",
                        pathParameters(
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포인트 계좌(Account) ID"),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("totalAmount").description("포인트 총계(최초 가입시 0원)")
                        )
                ));
    }

    @Test
    @DisplayName("포인트를 관리하기 위한 적립금 계정을 생성하는 테스트입니다. 이미 계정이 존재하고 있어 실패합니다. (createAccount 메서드 테스트)")
    void createAmountFailTest() throws Exception {
        // given
        Long memberId = 1L;
        AccumulatedPoint accumulatedPoint = AccumulatedPoint.builder().memberId(memberId).totalAmount(0).build();
        accumulatedPointPointRepository.save(accumulatedPoint);
        // expect
        mockMvc.perform(post("/reserves/create/{memberId}", memberId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(document(
                        "account_create_fail",
                        pathParameters(
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("409 에러코드를 반환합니다."),
                                fieldWithPath("message").description("'이미 존재하는 계정입니다. (향후 하나의 계정이 여러 포인트를 사용하도록 개선 예정입니다.)' 메시지를 반환합니다."),
                                fieldWithPath("validation.errorResponse").description("위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.")
                        )
                ));
    }

    @Test
    @DisplayName("포인트를 관리하기 위한 포인트 계정을 삭제하는 테스트입니다. (deleteAccount 메서드 테스트)")
    void deleteAccountTest() throws Exception {
        //given
        Long memberId = 1L;
        AccumulatedPoint accumulatedPoint = AccumulatedPoint.builder().memberId(memberId).totalAmount(0).build();
        accumulatedPointPointRepository.save(accumulatedPoint);

        // expect
        mockMvc.perform(delete("/reserves/delete/{memberId}",memberId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(
                        "account_delete",
                        pathParameters(
                                parameterWithName("memberId").description("회원번호")
                        )
                ));
    }

    @Test
    @DisplayName("포인트를 관리하기 위한 포인트 계정을 삭제하는 테스트이며 실패 케이스 테스트입니다. (deleteAccount 메서드 테스트)")
    void deleteAccountFailTest() throws Exception {
        // given 없음
        // expect
        mockMvc.perform(get("/reserves/get/{memberId}",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document(
                        "account_delete_fail",
                        pathParameters(
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("404 error를 반환합니다."),
                                fieldWithPath("message").description("'존재하지 않는 계좌입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.errorMessage").description("위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.")
                        )
                ));
    }

    @Test
    @DisplayName("회원별 포인트 합계를 조회하는 테스트입니다. (getAccount 메서드 테스트)")
    void getAccountTest() throws Exception {
        //given
        Long memberId = 1L;
        int totalAmount = 100;
        AccumulatedPoint accumulatedPoint = new AccumulatedPoint(memberId, totalAmount);
        accumulatedPointPointRepository.save(accumulatedPoint);
        // expect
        mockMvc.perform(get("/reserves/get/{memberId}",memberId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(totalAmount))
                .andDo(document(
                        "account_get_account",
                        pathParameters(
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포인트 계좌(Account) ID"),
                                fieldWithPath("memberId").description("회원번호"),
                                fieldWithPath("totalAmount").description("포인트 총계(현재 가지고 있는 포인트의 합을 반환합니다.)")
                        )
                ));
    }

    @Test
    @DisplayName("회원별 포인트 합계를 조회하는 테스트이며 회원정보가 없어 실패하는 케이스입니다.  (getAccount 메서드 테스트)")
    void getAccountFailTest() throws Exception {
        // expect
        mockMvc.perform(get("/reserves/get/{memberId}",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document(
                        "account_get_account_fail",
                        pathParameters(
                                parameterWithName("memberId").description("회원번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("404를 반환합니다."),
                                fieldWithPath("message").description("'존재하지 않는 계좌입니다.' 문구를 반환합니다."),
                                fieldWithPath("validation.errorMessage").description("위 message와 동일한 문구입니다. 다른 에러가 중복 발생할 경우 다른 에러 정보도 포함됩니다.")
                        )
                ));
    }

}
package com.cherrydev.cherrymarketbe.admin;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserRole;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserStatus;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.cherrydev.cherrymarketbe.factory.AdminFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;

    private AccountDetails accountDetails;
    private JwtResponseDto jwtResponseDto;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AccountDetails) {
            accountDetails = (AccountDetails) authentication.getPrincipal();
            jwtResponseDto = jwtProvider.createJwtToken(accountDetails.getUsername());
        }
    }


    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_조회_성공() throws Exception {
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/admin/account-list")
                        .secure(true)
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").exists())
                .andDo(document("Admin-Get-Account-List",
                        resourceDetails()
                                .tag("관리자")
                                .description("계정 목록 조회"),
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("page").optional().description("페이지 번호"),
                                RequestDocumentation.parameterWithName("size").optional().description("한 페이지에 보여줄 항목 수")
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_권한_변경_성공() throws Exception {
        // Given
        ModifyUserRole modifyUserRole = createModifyUserRoleRequestDtoA();
        String requestBody = Jackson.toJsonString(modifyUserRole);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/admin/modify/account/role")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Admin-Modify-Account-Role",
                        resourceDetails()
                                .tag("관리자")
                                .description("계정 권한 변경")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_상태_변경_성공() throws Exception {
        // Given
        ModifyUserStatus modifyUserStatus = createModifyUserStatusByAdminDtoA();
        String requestBody = Jackson.toJsonString(modifyUserStatus);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/admin/modify/account/status")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Admin-Modify-Account-Status",
                        resourceDetails()
                                .tag("관리자")
                                .description("계정 상태 변경")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_상태_변경_실패_날짜_누락() throws Exception {
        // Given
        ModifyUserStatus modifyUserStatus = createModifyUserStatusByAdminDtoB();
        String requestBody = Jackson.toJsonString(modifyUserStatus);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/admin/modify/account/status")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Admin-Modify-Account-Status-Error",
                        resourceDetails()
                                .tag("관리자")
                                .description("계정 상태 변경 실패 - 날짜 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(INVALID_INPUT_VALUE.getStatusCode()),
                                fieldWithPath("message").description(INVALID_INPUT_VALUE.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_상태_변경_실패_날짜_오류() throws Exception {
        // Given
        ModifyUserStatus modifyUserStatus = createModifyUserStatusByAdminDtoC();
        String requestBody = Jackson.toJsonString(modifyUserStatus);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/admin/modify/account/status")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Admin-Modify-Account-Status-Error",
                        resourceDetails()
                                .tag("관리자")
                                .description("계정 상태 변경 실패 - 날짜 오류"),
                        responseFields(
                                fieldWithPath("statusCode").description(INVALID_INPUT_VALUE.getStatusCode()),
                                fieldWithPath("message").description(INVALID_INPUT_VALUE.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 포인트_지급_성공() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoA());

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Admin-Grant-Reward",
                        resourceDetails()
                                .tag("관리자")
                                .description("포인트 지급")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 포인트_지급_실패_없는_계정() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoB());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Admin-Grant-Reward-Error",
                        resourceDetails()
                                .tag("관리자")
                                .description("포인트 지급 실패 - 없는 계정"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_FOUND_ACCOUNT.getStatusCode()),
                                fieldWithPath("message").description(NOT_FOUND_ACCOUNT.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_발급_성공() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createIssueCouponRequestDtoA());

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/issue-coupon")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Admin-Issue-Coupon",
                        resourceDetails()
                                .tag("관리자")
                                .description("쿠폰 발급")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_발급_실패_코드_중복() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createIssueCouponRequestDtoB());

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/issue-coupon")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Admin-Issue-Coupon-Error",
                        resourceDetails()
                                .tag("관리자")
                                .description("쿠폰 발급 실패 - 코드 중복"),
                        responseFields(
                                fieldWithPath("statusCode").description(INVALID_INPUT_VALUE.getStatusCode()),
                                fieldWithPath("message").description(INVALID_INPUT_VALUE.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_목록_조회_성공() throws Exception {
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/admin/coupon-list")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").exists())
                .andDo(document("Admin-Get-Coupon-List",
                        resourceDetails()
                                .tag("관리자")
                                .description("쿠폰 목록 조회")));
    }


    @Test
    @Transactional
    @WithUserDetails(value = "ibag@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 요청_권한_없음() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoA());

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(document("Admin-Grant-Reward-Error",
                        resourceDetails()
                                .tag("관리자")
                                .description("요청 권한 없음"),
                        responseFields(
                                fieldWithPath("statusCode").description("403"),
                                fieldWithPath("message").description("접근 권한이 없습니다.")
                        )));
    }

    @Test
    @Transactional
    @WithAnonymousUser
    void 인증_수단_없음() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoA());

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(document("Admin-Grant-Reward-Error",
                        resourceDetails()
                                .tag("관리자")
                                .description("인증 수단 없음"),
                        responseFields(
                                fieldWithPath("statusCode").description("401"),
                                fieldWithPath("message").description("인증이 필요한 서비스입니다.")
                        )));
    }
}

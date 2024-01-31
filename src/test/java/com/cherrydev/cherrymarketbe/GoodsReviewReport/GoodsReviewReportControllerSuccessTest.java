package com.cherrydev.cherrymarketbe.GoodsReviewReport;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.goodsreview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsreviewreport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsreviewreport.dto.ReviewReportRequestDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.cherrydev.cherrymarketbe.factory.AuthFactory.createSignInRequestDtoF;
import static com.cherrydev.cherrymarketbe.factory.GoodsReviewReportFactory.createReviewReportRequestDtoA;
import static com.cherrydev.cherrymarketbe.factory.GoodsReviewReportFactory.createReviewReportRequestDtoD;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class GoodsReviewReportControllerSuccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;

    private GoodsReview goodsReview;

    private Account account;
    private AccountDetails accountDetails;
    private JwtResponseDto jwtResponseDto;
    private JwtRequestDto jwtRequestDto;


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
            jwtRequestDto = new JwtRequestDto(jwtResponseDto.getAccessToken(), jwtResponseDto.getRefreshToken());
            account = accountDetails.getAccount();
        }
    }


    @Test
    @Transactional
    void 로그인_성공() throws Exception {
        // Given
        RequestSignIn requestSignIn = createSignInRequestDtoF();
        String requestBody = Jackson.toJsonString(requestSignIn);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value("ROLE_CUSTOMER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grantType").value("Bearer "))
                .andExpect(MockMvcResultMatchers.header().exists("Set-Cookie"))
                .andDo(document("Sign-In-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("로그인 성공"),
                        responseFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("userRole").description("사용자 권한"),
                                fieldWithPath("grantType").description("토큰 타입"),
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레시 토큰"),
                                fieldWithPath("expiresIn").description("토큰 만료 시간")
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_등록_성공() throws Exception {
        // Given
        ReviewReportRequestDto reviewReportRequestDto = createReviewReportRequestDtoA();
        String requestBody = Jackson.toJsonString(reviewReportRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/review-report/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(document("Add-GoodsReviewReport-Success",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 - 등록")
                ));
    }



    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_조회_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/review-report/search?reportId=80")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportType").exists())
                .andDo(document("Get-GoodsReviewReport-Info",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품 후기 신고 - 조회")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_전체_조회() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/review-report/list")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").exists())
                .andDo(document("Get-GoodsReviewReportReport-Info-List",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 - 전체 조회")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_전체_조회_답변() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/review-report/list-status")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").exists())
                .andDo(document("Get-GoodsReviewReportReport-Info-List-By-ExistAnswer",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 - 전체 조회(답변 유무)")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_답변_등록_성공() throws Exception {
        // Given
        ReviewReportModifyDto reviewReportModifyDto = createReviewReportRequestDtoD();
        String requestBody = Jackson.toJsonString(reviewReportModifyDto);
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/review-report/add-answer")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("A-Modify-GoodsReviewReport-Answer",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 답변 - 등록")

                ));
    }
}


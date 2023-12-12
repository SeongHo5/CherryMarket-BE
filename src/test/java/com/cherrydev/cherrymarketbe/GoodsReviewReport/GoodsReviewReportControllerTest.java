package com.cherrydev.cherrymarketbe.GoodsReviewReport;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
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

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_REPORT;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_ALLOWED_EMPTY_CONTENT;
import static com.cherrydev.cherrymarketbe.factory.AuthFactory.createSignInRequestDtoA;
import static com.cherrydev.cherrymarketbe.factory.GoodsReviewReportFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class GoodsReviewReportControllerTest {

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
        SignInRequestDto signInRequestDto = createSignInRequestDtoA();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value("ROLE_CUSTOMER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grantType").value("Bearer "))
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
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_중복_실패() throws Exception {
        // Given
        ReviewReportRequestDto reviewReportRequestDto = createReviewReportRequestDtoF();
        String requestBody = Jackson.toJsonString(reviewReportRequestDto);


        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/review-report/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Add-GoodsReviewReport-Failed-Duplicated-Exception",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 등록 실패 - 중복 신고"),
                        responseFields(
                                fieldWithPath("statusCode").description(ALREADY_EXIST_REPORT.getStatusCode()),
                                fieldWithPath("message").description(ALREADY_EXIST_REPORT.getMessage())
                        )));

    }


    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_등록_실패_내용누락() throws Exception {
        // Given
        ReviewReportRequestDto reviewReportRequestDto = createReviewReportRequestDtoB();
        String requestBody = Jackson.toJsonString(reviewReportRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Add-GoodsReview-Failed-Duplicated-Exception",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품 후기 등록 실패 - 내용 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_등록_실패_카테고리_누락() throws Exception {
        // Given
        ReviewReportRequestDto reviewReportRequestDto = createReviewReportRequestDtoC();
        String requestBody = Jackson.toJsonString(reviewReportRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Add-GoodsReview-Failed-Duplicated-Exception",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품 후기 등록 실패 - 카테고리 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));

    }





    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_조회_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/review-report/search?reportId=11")
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].reportId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].reviewId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userId").value(159))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].reportType").value("FLOODING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].content").value("도배글 같아요 신고합니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].answerContent").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].answerStatus").value("NOT_EXIST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].createDate").value("2023-12-09 23:44:03"))
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
                .andDo(document("Modify-GoodsReviewReport-Answer",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 답변 - 등록")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_답변_등록_실패() throws Exception {
        // Given
        ReviewReportModifyDto reviewReportModifyDto = createReviewReportRequestDtoE();
        String requestBody = Jackson.toJsonString(reviewReportModifyDto);
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/review-report/add-answer")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Modify-GoodsReviewReport-Answer",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 답변 - 등록 실패 - 답변 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));
    }
}


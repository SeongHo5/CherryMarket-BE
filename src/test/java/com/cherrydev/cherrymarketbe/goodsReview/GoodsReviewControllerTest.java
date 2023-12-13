package com.cherrydev.cherrymarketbe.goodsReview;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
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

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.factory.AuthFactory.createSignInRequestDtoA;
import static com.cherrydev.cherrymarketbe.factory.GoodsReviewFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class GoodsReviewControllerTest {

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
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_등록_성공() throws Exception {

        // Given
        GoodsReviewRequestDto goodsReviewRequestDto = createGoodsReviewRequestDtoA();
        String requestBody = Jackson.toJsonString(goodsReviewRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(document("Add-GoodsReview-Success",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 등록 성공")
                        ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_등록_실패_중복리뷰() throws Exception {

        // Given
        GoodsReviewRequestDto goodsReviewRequestDto = createGoodsReviewRequestDtoD();
        String requestBody = Jackson.toJsonString(goodsReviewRequestDto);


        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Add-GoodsReview-Failed-Duplicated-Exception",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 등록 실패 - 게시글 소유자가 아님"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_POST_OWNER.getStatusCode()),
                                fieldWithPath("message").description(NOT_POST_OWNER.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_등록_실패_소유자_아님() throws Exception {
        // Given
        GoodsReviewRequestDto goodsReviewRequestDto = createGoodsReviewRequestDtoD();
        String requestBody = Jackson.toJsonString(goodsReviewRequestDto);


        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Add-GoodsReview-Failed-Not-Post-Owner",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 등록 실패 - 중복리뷰 존재"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_POST_OWNER.getStatusCode()),
                                fieldWithPath("message").description(NOT_POST_OWNER.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_등록_실패_내용누락() throws Exception {
        // Given
        GoodsReviewRequestDto goodsReviewRequestDto = createGoodsReviewRequestDtoC();
        String requestBody = Jackson.toJsonString(goodsReviewRequestDto);


        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Add-GoodsReview-Failed-Null-Content",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 등록 실패 - 내용 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_조회_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/search?ordersId=4&goodsId=2")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.goodsId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").exists())
                .andDo(document("Get-GoodsReview-Info",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 조회")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_전체조회_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/list")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-GoodsReview-List-Info",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 전체 조회")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_전체조회_상품별_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/list-goods?goodsId=22")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].reviewId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].ordersId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].goodsId").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userId").value(21))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].code").value("GRV3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].isBest").value("BEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].subject").value("가성비가 별로였어요"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].content").value("이건 조금 가성비가 별로였어요"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status").value("ACTIVE"))
                .andDo(document("Get-GoodsReview-List-Info-By-Goods",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 전체 조회 - 상품별")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_전체조회_주문별_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/list-order?ordersId=4")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-GoodsReview-List-Info-By-Orders",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 전체 조회 - 주문별")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_전체조회_유저별_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/list-user?userId=21")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-GoodsReview-List-Info-By-User",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 전체 조회 - 유저별")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_전체조회_내상품_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/list-my")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-GoodsReview-My-List-Info",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 전체 조회 - 내 리뷰 보기")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_삭제_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/goods-review/delete?ordersId=17&goodsId=5")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Delete-GoodsReview",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 - 삭제")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_삭제_실패_게시글_권한() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/goods-review/delete?ordersId=4&goodsId=33")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andDo(document("Delete-GoodsReview-Failed-Not-Post-Owner",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 삭제 실패- 게시글 소유자"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_POST_OWNER.getStatusCode()),
                                fieldWithPath("message").description(NOT_POST_OWNER.getMessage())
                        )));

    }



    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_수정_성공() throws Exception {
        // Given
        GoodsReviewModifyDto modifyInquiryRequestDto = createGoodsReviewModifyDto();
        String requestBody = Jackson.toJsonString(modifyInquiryRequestDto);
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/goods-review/modify")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Modify-GoodsReview",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 - 수정")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "sanghyeongim@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_수정_실패_게시글_권한() throws Exception {

        // Given
        GoodsReviewModifyDto modifyInquiryRequestDto = createGoodsReviewModifyDto();
        String requestBody = Jackson.toJsonString(modifyInquiryRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/goods-review/modify")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andDo(document("Modify-GoodsReview-Failed-Not-Post-Owner",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 수정 실패- 게시글 소유자"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_POST_OWNER.getStatusCode()),
                                fieldWithPath("message").description(NOT_POST_OWNER.getMessage())
                        )));

    }

}


package com.cherrydev.cherrymarketbe.goodsReview;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
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

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_ALLOWED_EMPTY_CONTENT;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_POST_OWNER;
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
class GoodsReviewControllerFailureTest {

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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
                .andDo(document("Failed-Add-GoodsReview-Duplicated-Exception",
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
                .andDo(document("Failed-Add-GoodsReview-Not-Post-Owner",
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
                .andDo(document("Failed-Add-GoodsReview-Empty-Content",
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
    void 상품후기_삭제_실패_게시글_권한() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/goods-review/delete?ordersId=122&goodsId=10")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andDo(document("Failed-Delete-GoodsReview-Not-Post-Owner",
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
                .andDo(document("Failed-Modify-GoodsReview-Not-Post-Owner",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 수정 실패- 게시글 소유자"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_POST_OWNER.getStatusCode()),
                                fieldWithPath("message").description(NOT_POST_OWNER.getMessage())
                        )));

    }

}


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

import static com.cherrydev.cherrymarketbe.factory.GoodsReviewFactory.createGoodsReviewModifyDto;
import static com.cherrydev.cherrymarketbe.factory.GoodsReviewFactory.createGoodsReviewRequestDtoA;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class GoodsReviewControllerSuccessTest {

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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_조회_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/search?ordersId=122&goodsId=10")
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_전체조회_상품별_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goods-review/list-goods?goodsId=20")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-GoodsReview-List-Info-By-Goods",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 전체 조회 - 상품별")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_삭제_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/goods-review/delete?ordersId=122&goodsId=30")
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
                .andDo(document("A-Modify-GoodsReview",
                        resourceDetails()
                                .tag("상품 후기")
                                .description("상품 후기 - 수정")

                ));
    }

}


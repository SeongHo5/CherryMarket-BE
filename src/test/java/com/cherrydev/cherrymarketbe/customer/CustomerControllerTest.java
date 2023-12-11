package com.cherrydev.cherrymarketbe.customer;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.ModifyAddressRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.factory.CustomerFactory.createAddAddressRequestDtoA;
import static com.cherrydev.cherrymarketbe.factory.CustomerFactory.createModifyAddressRequestDtoA;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class CustomerControllerTest {

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
    @WithUserDetails(value = "yugyeongja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 내_리워드_정보_조회() throws Exception {
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/customer/reward/summary")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rewards").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.summary").isNotEmpty())
                .andDo(document("Customer-My-Reward-Summary",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("내 리워드 정보 조회")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "bagsunja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 내_주소록_조회() throws Exception {
        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/customer/address/my-list")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andDo(document("Customer-My-Address-List",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("내 주소록 조회")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "jeongung18@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 배송지_추가_성공() throws Exception {
        // Given
        AddAddressRequestDto requestDto = createAddAddressRequestDtoA();
        String requestBody = Jackson.toJsonString(requestDto);

        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/customer/address/add")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Customer-Add-Address",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("배송지 추가")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "bagsunja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 배송지_추가_실패_최대_개수_초과() throws Exception {
        // Given
        AddAddressRequestDto requestDto = createAddAddressRequestDtoA();
        String requestBody = Jackson.toJsonString(requestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/customer/address/add")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(document("Customer-Add-Address-Exceed-Count",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("배송지 추가 실패 - 최대 개수 초과"),
                        responseFields(
                                fieldWithPath("statusCode").description(ADDRESS_COUNT_EXCEEDED.getStatusCode()),
                                fieldWithPath("message").description(ADDRESS_COUNT_EXCEEDED.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ocoe@example.net", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 배송지_추가_실패_기본_배송지_존재() throws Exception {
        // Given
        AddAddressRequestDto requestDto = createAddAddressRequestDtoA();
        String requestBody = Jackson.toJsonString(requestDto);

        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/customer/address/add")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(document("Customer-Add-Address-Default-Exist",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("배송지 추가 실패 - 기본 배송지 존재"),
                        responseFields(
                                fieldWithPath("statusCode").description(DEFAULT_ADDRESS_ALREADY_EXISTS.getStatusCode()),
                                fieldWithPath("message").description(DEFAULT_ADDRESS_ALREADY_EXISTS.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "bagsunja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 배송지_삭제_성공() throws Exception {
        // Given
        Long addressId = 1L;

        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/customer/address/drop")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("addressId", String.valueOf(addressId))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Customer-Drop-Address",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("배송지 삭제")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "bagsunja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 배송지_수정_성공() throws Exception {
        // Given
        ModifyAddressRequestDto requestDto = createModifyAddressRequestDtoA();
        String requestBody = Jackson.toJsonString(requestDto);

        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/api/customer/address/modify")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Customer-Modify-Address",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("배송지 수정")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ocoe@example.net", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_등록_성공() throws Exception {
        // Given
        String couponCode = "FIRORD00";

        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/customer/register-coupon")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("couponCode", couponCode)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Customer-Register-Coupon",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("쿠폰 등록")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "hseong@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_등록_실패_이미_보유() throws Exception {
        // Given
        String couponCode = "WELCO0ME";

        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/customer/register-coupon")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("couponCode", couponCode)
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(document("Customer-Register-Coupon-Already-Exists",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("쿠폰 등록 실패 - 이미 보유"),
                        responseFields(
                                fieldWithPath("statusCode").description(ALREADY_EXIST_COUPON.getStatusCode()),
                                fieldWithPath("message").description(ALREADY_EXIST_COUPON.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ocoe@example.net", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_등록_없는_코드() throws Exception {
        // Given
        String couponCode = "NONE1515";

        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/customer/register-coupon")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("couponCode", couponCode)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(document("Customer-Register-Coupon-Not-Found",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("쿠폰 등록 실패 - 없는 코드"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_FOUND_COUPON.getStatusCode()),
                                fieldWithPath("message").description(NOT_FOUND_COUPON.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ogsungim@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_목록_조회_성공() throws Exception {
        // When & Then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/customer/coupon/list")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andDo(document("Customer-Coupon-List",
                        resourceDetails()
                                .tag("고객 관련")
                                .description("쿠폰 목록 조회")));
    }

}

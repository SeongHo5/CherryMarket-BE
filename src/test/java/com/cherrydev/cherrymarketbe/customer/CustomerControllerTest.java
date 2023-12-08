package com.cherrydev.cherrymarketbe.customer;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.ModifyAddressRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import static com.cherrydev.cherrymarketbe.factory.CustomerFactory.createAddAddressRequestDtoA;
import static com.cherrydev.cherrymarketbe.factory.CustomerFactory.createModifyAddressRequestDtoA;

@Rollback
@AutoConfigureMockMvc
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
    public void setup() {
        // Given
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
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
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/customer/reward/summary")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rewards").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.summary").isNotEmpty());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "bagsunja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 내_주소록_조회() throws Exception {
        // When & Then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/customer/address/my-list")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
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
                        MockMvcRequestBuilders.post("/api/customer/address/add")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "bagsunja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 배송지_추가_실패_최대_개수_초과() throws Exception {
        // Given
        AddAddressRequestDto requestDto = createAddAddressRequestDtoA();
        String requestBody = Jackson.toJsonString(requestDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer/address/add")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
                        MockMvcRequestBuilders.post("/api/customer/address/add")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "bagsunja@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 배송지_삭제_성공() throws Exception {
        // Given
        Long addressId = 1L;

        // When & Then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/customer/address/drop")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("addressId", String.valueOf(addressId))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
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
                        MockMvcRequestBuilders.patch("/api/customer/address/modify")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ocoe@example.net", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_등록_성공() throws Exception {
        // Given
        String couponCode = "FIRORD00";

        // When & Then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/customer/register-coupon")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("couponCode", couponCode)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "hseong@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_등록_실패_이미_보유() throws Exception {
        // Given
        String couponCode = "WELCO0ME";

        // When & Then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/customer/register-coupon")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("couponCode", couponCode)
                )
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ocoe@example.net", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 쿠폰_등록_없는_코드() throws Exception {
        // Given
        String couponCode = "NONE1515";

        // When & Then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/customer/register-coupon")
                                .secure(true)
                                .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                                .contentType("application/json")
                                .accept("application/json")
                                .param("couponCode", couponCode)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}

package com.cherrydev.cherrymarketbe.auth;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.cherrydev.cherrymarketbe.factory.AuthFactory.*;

@Rollback
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;

    private Account account;
    private AccountDetails accountDetails;
    private JwtResponseDto jwtResponseDto;

    private JwtRequestDto jwtRequestDto;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AccountDetails) {
            accountDetails = (AccountDetails) authentication.getPrincipal();
            jwtResponseDto = jwtProvider.createJwtToken(accountDetails.getUsername());
            jwtRequestDto = JwtRequestDto.builder()
                    .accessToken(jwtResponseDto.getAccessToken())
                    .refreshToken(jwtResponseDto.getRefreshToken())
                    .build();
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value("ROLE_CUSTOMER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grantType").value("Bearer "));
    }

    @Test
    @Transactional
    void 로그인_실패_비밀번호_불일치() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoB();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    void 로그인_실패_존재하지_않는_이메일() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoC();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    void 로그인_실패_제한된_사용자() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoD();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void 로그인_실패_탈퇴한_사용자() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoE();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 로그아웃_성공() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(jwtRequestDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/sign-out")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

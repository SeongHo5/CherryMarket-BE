package com.cherrydev.cherrymarketbe.account;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.dto.ModifyAccountInfoRequestDto;
import com.cherrydev.cherrymarketbe.account.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.factory.AccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.cherrydev.cherrymarketbe.factory.AccountFactory.createModifyAccountInfoRequestDtoA;

@Rollback
@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;

    private Account account;
    private AccountDetails accountDetails;
    private JwtResponseDto jwtResponseDto;

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
            account = accountDetails.getAccount();
        }
    }

    @Test
    @Transactional
    @WithAnonymousUser
    void 회원가입_Request_성공() throws Exception {
        // Given
        SignUpRequestDto signUpRequestDto = AccountFactory.createSignUpRequestDtoC();
        String requestBody = Jackson.toJsonString(signUpRequestDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/sign-up")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 내정보_조회_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/my-info")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(account.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contact").value(account.getContact()));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ubag@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 내정보_수정_성공() throws Exception {
        // Given
        ModifyAccountInfoRequestDto modifyAccountInfoRequestDto = createModifyAccountInfoRequestDtoA();

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/account/my-info/modify")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jackson.toJsonString(modifyAccountInfoRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(account.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contact").value(account.getContact()));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ogja39@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 회원탈퇴_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/drop-out")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}


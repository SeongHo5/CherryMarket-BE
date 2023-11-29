package com.cherrydev.cherrymarketbe.account;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.factory.AccountFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
@Rollback
@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @WithAnonymousUser
    void 회원가입_Request_성공() throws Exception {
        // Given
        SignUpRequestDto signUpRequestDto = AccountFactory.createSignUpRequestDtoA();
        String requestBody = Jackson.toJsonString(signUpRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com")
    void 내정보_조회_성공() throws Exception {
        // Given

    }



}

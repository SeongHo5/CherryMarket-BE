package com.cherrydev.cherrymarketbe.account;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.*;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.factory.AccountFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
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
            account = accountDetails.getAccount();
        }
    }

    @Test
    @Transactional
    @WithAnonymousUser
    void 회원가입_성공() throws Exception {
        // Given
        SignUpRequestDto signUpRequestDto = createSignUpRequestDtoC();
        String requestBody = Jackson.toJsonString(signUpRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/sign-up")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(document("Sign-Up-Success",
                        resourceDetails()
                                .tag("계정 관리")
                                .description("회원 가입 성공")
                        ));
    }

    @Test
    @Transactional
    @WithAnonymousUser
    void 회원가입_실패_금지된_이름() throws Exception {
        // Given
        SignUpRequestDto signUpRequestDto = createSignUpRequestDtoB();
        String requestBody = Jackson.toJsonString(signUpRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/sign-up")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Sign-Up-Failed-Prohibited-Username",
                        resourceDetails()
                                .tag("계정 관리")
                                .description("회원 가입 실패 - 금지된 이름"),
                        responseFields(
                                fieldWithPath("statusCode").description(PROHIBITED_USERNAME.getStatusCode()),
                                fieldWithPath("message").description(PROHIBITED_USERNAME.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithAnonymousUser
    void 회원가입_실패_이메일_중복() throws Exception {
        // Given
        SignUpRequestDto signUpRequestDto = createSignUpRequestDtoA();
        String requestBody = Jackson.toJsonString(signUpRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/sign-up")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Sign-Up-Failed-Duplicated-Email",
                        resourceDetails()
                                .tag("계정 관리")
                                .description("회원 가입 실패 - 이메일 중복"),
                        responseFields(
                                fieldWithPath("statusCode").description(CONFLICT_ACCOUNT.getStatusCode()),
                                fieldWithPath("message").description(CONFLICT_ACCOUNT.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 내정보_조회_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/account/my-info")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(account.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contact").value(account.getContact()))
                .andDo(document("Get-My-Info",
                        resourceDetails()
                                .tag("계정 관리")
                                .description("내 정보 조회"),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("birthdate").description("생년월일")
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ubag@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 내정보_수정_성공() throws Exception {
        // Given
        ModifyAccountInfoRequestDto modifyAccountInfoRequestDto = createModifyAccountInfoRequestDtoA();

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/account/my-info/modify")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jackson.toJsonString(modifyAccountInfoRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(account.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(account.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contact").value(account.getContact()))
                .andDo(document("Modify-My-Info",
                        resourceDetails()
                                .tag("계정 관리")
                                .description("내 정보 수정"),
                        requestFields(
                                fieldWithPath("password").optional().description("비밀번호"),
                                fieldWithPath("contact").optional().description("연락처"),
                                fieldWithPath("birthdate").optional().description("생년월일")
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ogja39@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 회원탈퇴_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/account/drop-out")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Drop-Out",
                        resourceDetails()
                                .tag("계정 관리")
                                .description("회원 탈퇴")));
    }



    @Test
    @Transactional
    @WithUserDetails(value = "ogja39@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 이메일중복_검사_성공() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/account/check-email?email=noyeongjin@example.org")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(CONFLICT_ACCOUNT.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CONFLICT_ACCOUNT.getMessage()))
                .andDo(document("Get-Duplicate-Email-Info",
                        resourceDetails()
                                .tag("계정 관리")
                                .description("이메일 중복검사"),
                        responseFields(
                                fieldWithPath("statusCode").description(CONFLICT_ACCOUNT.getStatusCode()),
                                fieldWithPath("message").description(CONFLICT_ACCOUNT.getMessage())
                        )));
    }
}


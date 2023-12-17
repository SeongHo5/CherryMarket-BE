package com.cherrydev.cherrymarketbe.notice;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.factory.NoticeFactory;
import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
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
import org.springframework.restdocs.payload.JsonFieldType;
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

import static com.cherrydev.cherrymarketbe.factory.NoticeFactory.createModifyNoticeInfoA;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class NoticeControllerSuccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;

    private Notice notice;

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
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_등록_성공() throws Exception {
        // Given
        NoticeRequestDto noticeRequestDto = NoticeFactory.createNoticeA();
        String requestBody = Jackson.toJsonString(noticeRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(document("Add-Notice-Success",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 등록 성공")
                        ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_조회_아이디_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/notice/notice-info/search-id?noticeId=19")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.noticeId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").exists())
                .andDo(document("Get-Notice-Info-Id",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 조회 - 아이디")

                ));
    }


    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_전체조회_코드_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/notice/notice-list")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-Notice-List-Info",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 전체 조회")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_삭제_코드_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/notice/delete-notice-code?code=NT7")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Delete-Notice-By-Code",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 삭제 - 코드")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_삭제_아이디_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/notice/delete-notice-id?noticeId=49")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Delete-Notice-By-Id",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 삭제 - 아이디")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_수정_아이디_성공() throws Exception {
        // Given
        ModifyNoticeInfoRequestDto modifyNoticeInfoRequestDto = createModifyNoticeInfoA();
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/notice/modify-id")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jackson.toJsonString(modifyNoticeInfoRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.noticeId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").exists())
                .andDo(document("A-Modify-Inquiry-Info-By-ID",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 수정 - 아이디"),
                        responseFields(
                                fieldWithPath("noticeId").optional().description("공지사항 ID"),
                                fieldWithPath("code").optional().description("공지사항 코드"),
                                fieldWithPath("category").optional().description("공지사항 카테고리"),
                                fieldWithPath("subject").optional().description("공지사항 제목"),
                                fieldWithPath("content").optional().description("공지사항 내용"),
                                fieldWithPath("status").optional().description("공지사항 상태"),
                                fieldWithPath("displayDate").optional().description("공지사항 노출 시작일"),
                                fieldWithPath("hideDate").optional().type(JsonFieldType.STRING).description("공지사항 노출 종료일"),
                                fieldWithPath("createDate").optional().type(JsonFieldType.STRING).description("공지사항 생성일"),
                                fieldWithPath("deleteDate").optional().type(JsonFieldType.STRING).description("공지사항 삭제일")
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_수정_코드_성공() throws Exception {
        // Given
        ModifyNoticeInfoRequestDto modifyNoticeInfoRequestDto = createModifyNoticeInfoA();
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/notice/modify-code")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jackson.toJsonString(modifyNoticeInfoRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.noticeId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject").exists())
                .andDo(document("A-Modify-Inquiry-Info-By-ID",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 수정 - 아이디"),
                        responseFields(
                                fieldWithPath("noticeId").optional().description("공지사항 ID"),
                                fieldWithPath("code").optional().description("공지사항 코드"),
                                fieldWithPath("category").optional().description("공지사항 카테고리"),
                                fieldWithPath("subject").optional().description("공지사항 제목"),
                                fieldWithPath("content").optional().description("공지사항 내용"),
                                fieldWithPath("status").optional().description("공지사항 상태"),
                                fieldWithPath("displayDate").optional().description("공지사항 노출 시작일"),
                                fieldWithPath("hideDate").optional().type(JsonFieldType.STRING).description("공지사항 노출 종료일"),
                                fieldWithPath("createDate").optional().type(JsonFieldType.STRING).description("공지사항 생성일"),
                                fieldWithPath("deleteDate").optional().type(JsonFieldType.STRING).description("공지사항 삭제일")
                        )));
    }


}


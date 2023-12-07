package com.cherrydev.cherrymarketbe.admin;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserRoleRequestDto;
import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserStatusByAdminDto;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
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

import static com.cherrydev.cherrymarketbe.factory.AdminFactory.*;

@Rollback
@AutoConfigureMockMvc
@SpringBootTest
public class AdminControllerTest {

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
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_조회_성공() throws Exception {
        // When & Then
        // OK를 반환하고, 페이징 처리된 계정 목록을 반환한다.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/account-list")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").exists());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_권한_변경_성공() throws Exception {
        // Given
        ModifyUserRoleRequestDto modifyUserRoleRequestDto = createModifyUserRoleRequestDtoA();
        String requestBody = Jackson.toJsonString(modifyUserRoleRequestDto);

        // When & Then
        // OK를 반환한다.
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/modify/account/role")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_상태_변경_성공() throws Exception {
        // Given
        ModifyUserStatusByAdminDto modifyUserStatusByAdminDto = createModifyUserStatusByAdminDtoA();
        String requestBody = Jackson.toJsonString(modifyUserStatusByAdminDto);

        // When & Then
        // OK를 반환한다.
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/modify/account/status")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_상태_변경_실패_날짜_누락() throws Exception {
        // Given
        ModifyUserStatusByAdminDto modifyUserStatusByAdminDto = createModifyUserStatusByAdminDtoB();
        String requestBody = Jackson.toJsonString(modifyUserStatusByAdminDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/modify/account/status")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 계정_상태_변경_실패_날짜_오류() throws Exception {
        // Given
        ModifyUserStatusByAdminDto modifyUserStatusByAdminDto = createModifyUserStatusByAdminDtoC();
        String requestBody = Jackson.toJsonString(modifyUserStatusByAdminDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/modify/account/status")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 포인트_지급_성공() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoA());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 포인트_지급_실패_없는_계정() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoB());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ibag@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 요청_권한_없음() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoA());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    @WithAnonymousUser
    void 인증_수단_없음() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(createAddRewardRequestDtoA());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/grant-reward")
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}

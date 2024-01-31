package com.cherrydev.cherrymarketbe.server.domain.account.entity;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.OAuthRequestDto;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole.ROLE_CUSTOMER;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long accountId;

    private RegisterType registType;

    private String oauthId;

    private String name;

    @Email
    private String email;

    private String password;

    private String contact;

    private Gender gender;

    private LocalDate birthdate;

    private UserRole userRole;

    private UserStatus userStatus;

    private LocalDate restrictedUntil;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Account of(RequestSignUp request, String encodedPassword, RegisterType registerType) {
        return Account.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .contact(request.getContact())
                .gender(Gender.valueOf(request.getGender()))
                .birthdate(LocalDate.parse(request.getBirthdate()))
                .userRole(ROLE_CUSTOMER)
                .registType(registerType)
                .build();
    }

    public static Account of(OAuthAccountInfo oAuthAccountInfo, String encodedPassword, RegisterType registerType) {
        return Account.builder()
                .name(oAuthAccountInfo.getName())
                .email(oAuthAccountInfo.getEmail())
                .password(encodedPassword)
                .userRole(ROLE_CUSTOMER)
                .registType(registerType)
                .build();
    }

    /**
     * MyBatis Mapper 용
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Account updatePassword(String encodePassword) {
        this.password = encodePassword;
        return this;
    }

    public void updateContact(String contact) {
        this.contact = contact;
    }

    public void updateBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    public void updateAccountRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void updateAccountStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void updateRestrictedUntil(LocalDate restrictedUntil) {
        this.restrictedUntil = restrictedUntil;
    }


}

package com.cherrydev.cherrymarketbe.account.entity;

import com.cherrydev.cherrymarketbe.account.enums.Gender;
import com.cherrydev.cherrymarketbe.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.account.enums.UserRole;
import com.cherrydev.cherrymarketbe.account.enums.UserStatus;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class Account {

    private Long accountId;

    private RegisterType registType;

    private Long oauthId;

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

    @Builder
    public Account(Long oauthId, String name, String email, String password,
                   String contact, Gender gender, LocalDate birthdate,
                   RegisterType registerType, UserRole userRole, UserStatus userStatus) {
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.gender = gender;
        this.birthdate = birthdate;
        this.registType = registerType;
        this.userRole = userRole;
        this.userStatus = userStatus;
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

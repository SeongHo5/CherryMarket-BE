package com.cherrydev.cherrymarketbe.server.domain.admin.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserStatus;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.sql.Timestamp;
import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.server.application.common.utils.TimeFormatter.timeStampToString;

@Value
@NoArgsConstructor(force = true)
public class AdminUserInfo {

    Long accountId;
    RegisterType registType;
    String name;
    String email;
    String contact;
    Gender gender;
    String birthdate;
    String createdAt;
    UserRole userRole;
    UserStatus userStatus;

    @Builder
    public AdminUserInfo(Long accountId, RegisterType registType, String name, String email,
                         String contact, Gender gender, LocalDate birthdate, Timestamp createdAt,
                         UserRole userRole, UserStatus userStatus) {
        this.accountId = accountId;
        this.registType = registType;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.gender = gender;
        this.birthdate = birthdate.toString();
        this.createdAt = timeStampToString(createdAt);
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

}

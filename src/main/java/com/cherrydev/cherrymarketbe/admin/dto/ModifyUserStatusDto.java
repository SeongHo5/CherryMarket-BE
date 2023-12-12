package com.cherrydev.cherrymarketbe.admin.dto;

import com.cherrydev.cherrymarketbe.account.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ModifyUserStatusDto {

    @Email
    @NotNull
    String email;

    @NotNull
    UserStatus newStatus;

    @NotNull
    String restrictedUntil;

    @Builder
    public ModifyUserStatusDto(String email, String newStatus, String restrictedUntil) {
        this.email = email;
        this.newStatus = UserStatus.valueOf(newStatus);
        this.restrictedUntil = restrictedUntil;
    }

}

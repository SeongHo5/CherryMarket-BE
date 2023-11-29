package com.cherrydev.cherrymarketbe.admin.dto;

import com.cherrydev.cherrymarketbe.account.enums.UserStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ModifyUserStatusByAdminDto {

    @Email
    @NotNull
    String email;

    @NotNull
    UserStatus newStatus;

    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    LocalDate restrictedUntil;

    public ModifyUserStatusByAdminDto(String email, String newStatus, LocalDate restrictedUntil) {
        this.email = email;
        this.newStatus = UserStatus.valueOf(newStatus);
        this.restrictedUntil = restrictedUntil;
    }

}

package com.cherrydev.cherrymarketbe.admin.dto;

import com.cherrydev.cherrymarketbe.account.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ModifyUserRoleRequestDto {

    @Email
    @NotNull
    String email;

    @NotNull
    UserRole newRole;

    public ModifyUserRoleRequestDto(String email, String newRole) {
        this.email = email;
        this.newRole = UserRole.valueOf(newRole);
    }

}

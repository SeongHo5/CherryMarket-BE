package com.cherrydev.cherrymarketbe.factory;



import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.SignUpRequestDto;

import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.account.enums.Gender.MALE;
import static com.cherrydev.cherrymarketbe.account.enums.UserRole.ROLE_CUSTOMER;
import static com.cherrydev.cherrymarketbe.account.enums.UserStatus.DELETED;
import static com.cherrydev.cherrymarketbe.account.enums.UserStatus.RESTRICTED;

public class AccountFactory {

    public static SignUpRequestDto createSignUpRequestDtoA() {
        return SignUpRequestDto.builder()
                .name("김영희")
                .email("test@example.com")
                .password("Testuser12#")
                .contact(("+82 10-1234-5678"))
                .gender("FEMALE")
                .birthdate("1990-01-01")
                .build();
    }

    public static SignUpRequestDto createSignUpRequestDtoB() {
        return SignUpRequestDto.builder()
                .name("관리자")
                .email("test@example.com")
                .password("Testuser12#")
                .contact(("+82 10-1234-5678"))
                .gender(("MALE"))
                .birthdate("1990-01-01")
                .build();
    }

    public static Account createAccountA() {
        return Account.builder()
                .name("김영희")
                .email("test@marketcherry.com")
                .password("Password12#")
                .contact("+82 10-1234-1234")
                .gender(MALE)
                .birthdate(LocalDate.parse("1990-01-01"))
                .userRole(ROLE_CUSTOMER)
                .userStatus(RESTRICTED)
                .build();
    }

    public static Account createAccountB() {
        return Account.builder()
                .name("김영희")
                .email("admin@marketcherry.com")
                .password("Admin12!@")
                .contact("+82 10-1234-1234")
                .gender(MALE)
                .birthdate(LocalDate.parse("1990-01-01"))
                .userRole(ROLE_CUSTOMER)
                .userStatus(DELETED)
                .build();
    }


}

package com.cherrydev.cherrymarketbe.account.dto;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.enums.Gender;
import lombok.Value;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.localDateToString;


@Value
public class AccountInfoDto {

    String name;
    String email;
    String contact;
    Gender gender;
    String birthdate;

    public AccountInfoDto(Account account) {
        this.name = account.getName();
        this.email = account.getEmail();
        this.contact = account.getContact();
        this.gender = account.getGender();
        this.birthdate = localDateToString(account.getBirthdate());
    }

}

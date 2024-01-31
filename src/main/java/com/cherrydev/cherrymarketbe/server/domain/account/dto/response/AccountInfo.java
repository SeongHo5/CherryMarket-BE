package com.cherrydev.cherrymarketbe.server.domain.account.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.Gender;
import lombok.Value;

import static com.cherrydev.cherrymarketbe.server.application.common.utils.TimeFormatter.localDateToString;


@Value
public class AccountInfo {

    String name;
    String email;
    String contact;
    Gender gender;
    String birthdate;

    public AccountInfo(Account account) {
        this.name = account.getName();
        this.email = account.getEmail();
        this.contact = account.getContact();
        this.gender = account.getGender();
        this.birthdate = localDateToString(account.getBirthdate());
    }

}

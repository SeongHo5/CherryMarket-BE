package com.cherrydev.cherrymarketbe.server.domain.maker.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class MakerInfo {

    Long makerId;

    String makerName;

    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 등록 번호 형식에 맞게 입력해 주세요")
    String businessNumber;

    String companyPhoneNumber;

    @Email
    String businessEmail;
}

package com.cherrydev.cherrymarketbe.maker.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MakerDto {

    Long makerId;
    String makerName;
    String businessNumber;
    String companyPhoneNumber;
    String businessEmail;
}

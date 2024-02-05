package com.cherrydev.cherrymarketbe.inquiry.dto;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryDetailType;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryType;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class InquiryRequestDto {

    Long userId;
    String code;
    String type;
    String detailType;
    String subject;
    String content;
    String status;
    String phone;

    public Inquiry toEntity() {
        return Inquiry.builder()
                .userId(this.getUserId())
                .code("001")
                .type(InquiryType.valueOf(this.getType()))
                .detailType(InquiryDetailType.valueOf(this.getDetailType()))
                .subject(this.getSubject())
                .content(this.getContent())
                .status(DisplayStatus.ACTIVE)
                .build();
    }

    public Inquiry toEntity(AccountDetails accountDetails) {
        return Inquiry.builder()
                .userId(accountDetails.getAccount().getAccountId())
                .code("001")
                .type(InquiryType.valueOf(this.getType()))
                .detailType(InquiryDetailType.valueOf(this.getDetailType()))
                .subject(this.getSubject())
                .content(this.getContent())
                .status(DisplayStatus.ACTIVE)
                .build();
    }
}

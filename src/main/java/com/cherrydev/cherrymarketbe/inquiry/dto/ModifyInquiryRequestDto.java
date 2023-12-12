package com.cherrydev.cherrymarketbe.inquiry.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ModifyInquiryRequestDto {
    Long userId;
    Long inquiryId;
    String code;
    String type;
    String detailType;
    String subject;
    String content;
    String phone;

    @Builder
    public ModifyInquiryRequestDto(Long userId, Long inquiryId, String code, String type,
                                   String detailType, String subject, String content, String phone) {
        this.userId = userId;
        this.inquiryId = inquiryId;
        this.code = code;
        this.type = type;
        this.detailType = detailType;
        this.subject = subject;
        this.content = content;
        this.phone = phone;
    }
}

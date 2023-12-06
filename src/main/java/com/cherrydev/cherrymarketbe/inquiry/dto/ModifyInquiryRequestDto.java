package com.cherrydev.cherrymarketbe.inquiry.dto;

import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryDetailType;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryType;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
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


    public ModifyInquiryRequestDto(Long userId, Long inquiryId, String code, String type, String detailType, String subject, String content, String phone) {
        this.userId = userId;
        this.inquiryId = inquiryId;
        this.code = code;
        this.type = type;
        this.detailType = detailType;
        this.subject = subject;
        this.content = content;
        this.phone = phone;
    }

//
//    public Inquiry ConvertForDB(){
//        return Inquiry.builder()
////                .userId(this.getUserId())
//                .inquiryId(this.getInquiryId())
//                .code(this.getCode())
//                .type(InquiryType.valueOf(this.getType()))
//                .detailType(InquiryDetailType.valueOf(this.getDetailType()))
//                .subject(this.getSubject())
//                .content(this.getContent())
//                .status(DisplayStatus.ACTIVE)
//                .phone(this.getPhone())
//                .build();
//    }
}

package com.cherrydev.cherrymarketbe.inquiry.entity;

import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryDetailType;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryType;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class Inquiry {
    private Long inquiryId;
    private Long userId;
    private String code;
    private InquiryType type;
    private InquiryDetailType detailType;
    private String subject;
    private String  content;
    private DisplayStatus status;
    private String  phone;
    private Timestamp createDate;
    private Timestamp deleteDate;

    @Builder
    public Inquiry(Long inquiryId, Long userId, String code, InquiryType type,
                   InquiryDetailType detailType, String subject, String content,
                   DisplayStatus status, String phone, Timestamp createDate, Timestamp deleteDate) {

        this.inquiryId = inquiryId;
        this.userId = userId;
        this.code = code;
        this.type = type;
        this.detailType = detailType;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.phone = phone;
        this.createDate = createDate;
        this.deleteDate = deleteDate;
    }


    public void updateTypeAndDetailType(InquiryType newType, InquiryDetailType newDetailType) {
        this.type = newType;
        this.detailType = newDetailType;
    }

    public void updateSubject(String subject) {
        this.subject = subject;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(DisplayStatus status) {
        this.status = status;
    }
}

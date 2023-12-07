package com.cherrydev.cherrymarketbe.inquiryAnswer.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class InquiryAnswer {
    private Long answerId;
    private Long inquiryId;
    private String memo;
    private String content;
    private Timestamp createDate;

    @Builder
    public InquiryAnswer(Long answerId, Long inquiryId, String memo, String content, Timestamp createDate) {
        this.answerId = answerId;
        this.inquiryId = inquiryId;
        this.memo = memo;
        this.content = content;
        this.createDate = createDate;
    }
}

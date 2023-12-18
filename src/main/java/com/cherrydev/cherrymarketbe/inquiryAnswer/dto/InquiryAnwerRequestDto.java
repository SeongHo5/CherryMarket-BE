package com.cherrydev.cherrymarketbe.inquiryAnswer.dto;

import com.cherrydev.cherrymarketbe.inquiryAnswer.entity.InquiryAnswer;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InquiryAnwerRequestDto {

    Long inquiryId;
    String memo;
    String content;

    public InquiryAnswer toEntity() {
        return InquiryAnswer.builder()
                .inquiryId(this.getInquiryId())
                .memo(this.getMemo())
                .content(this.getContent())
                .build();
    }

}

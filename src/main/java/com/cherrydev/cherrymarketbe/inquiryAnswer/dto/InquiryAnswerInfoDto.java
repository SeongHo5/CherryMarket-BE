package com.cherrydev.cherrymarketbe.inquiryAnswer.dto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.entity.InquiryAnswer;
import lombok.Getter;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;

@Getter
public class InquiryAnswerInfoDto {
    Long answerId;
    Long inquiryId;
    String memo;
    String content;
    String createDate;

    public InquiryAnswerInfoDto(InquiryAnswer inquiryAnswer) {
        this.answerId = inquiryAnswer.getAnswerId();
        this.inquiryId = inquiryAnswer.getInquiryId();
        this.memo = inquiryAnswer.getMemo();
        this.content = inquiryAnswer.getContent();
        this.createDate = timeStampToString(inquiryAnswer.getCreateDate());
    }

    public static List<InquiryAnswerInfoDto> convertToDtoList(List<InquiryAnswer> inquiryAnswers) {
        return inquiryAnswers.stream()
                .map(InquiryAnswerInfoDto::new)
                .toList();
    }
}

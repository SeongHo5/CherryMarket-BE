package com.cherrydev.cherrymarketbe.inquiryAnswer.repository;

import com.cherrydev.cherrymarketbe.inquiryAnswer.entity.InquiryAnswer;

public interface InquiryAnswerMapper {
    void save(InquiryAnswer inquiryAnswer);
    void updateStatue(Long inquiryId);
}

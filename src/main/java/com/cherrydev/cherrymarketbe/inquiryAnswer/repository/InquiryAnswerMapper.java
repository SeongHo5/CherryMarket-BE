package com.cherrydev.cherrymarketbe.inquiryAnswer.repository;

import com.cherrydev.cherrymarketbe.inquiryAnswer.entity.InquiryAnswer;

import java.util.List;

public interface InquiryAnswerMapper {
    void save(InquiryAnswer inquiryAnswer);

    void updateStatue(Long inquiryId);

    InquiryAnswer findById(Long inquiryId);

    List<InquiryAnswer> findAll();

    List<InquiryAnswer> findByUserId(Long userId);

    List<InquiryAnswer> findByEmail(String email);

}

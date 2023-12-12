package com.cherrydev.cherrymarketbe.inquiryAnswer.repository;

import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnswerInfoDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.entity.InquiryAnswer;

import java.util.List;

public interface InquiryAnswerMapper {
    void save(InquiryAnswer inquiryAnswer);

    void updateStatue(Long inquiryId);

    InquiryAnswer findById(Long inquiryId);

    List<InquiryAnswerInfoDto> findAll();

    List<InquiryAnswerInfoDto> findByUserId(Long userId);

    List<InquiryAnswerInfoDto> findByEmail(String email);

    void delete(Long answerId);

    void updateStatusAccepting(Long answerId);

    boolean existAnswer(Long inquiryId);

}

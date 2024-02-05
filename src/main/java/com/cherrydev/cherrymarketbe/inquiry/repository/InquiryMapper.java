package com.cherrydev.cherrymarketbe.inquiry.repository;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryMapper {
    void save(Inquiry inquiry);

    void deleteById(Long inquiryId);

    void deleteByCode(String inquiryCode);

    Inquiry findByInquiryId(Long inquiryId);

    Inquiry findByInquiryCode(String inquiryCode);

    List<InquiryInfoDto> findAll();

    List<InquiryInfoDto> findAllByUser(Long userId);

    List<InquiryInfoDto> findAllByPhone(String phone);

    List<InquiryInfoDto> findAllMyList(Long accountId);

    void updateStatusByDel(Inquiry inquiry);

    void update(Inquiry inquiry);

    void updateAnswerStatus(Inquiry inquiry);

    void updateAnswerStatusByInfo(InquiryInfoDto inquiryInfoDto);

    boolean existAnswerInquiry(Long inquiryId);

    boolean getUserId(Long inquiryId, Long userId);

    boolean getUserIdByCode(@Param("code") String code, @Param("userId") Long userId);
}

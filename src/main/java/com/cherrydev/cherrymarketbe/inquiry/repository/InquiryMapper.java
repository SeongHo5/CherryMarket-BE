package com.cherrydev.cherrymarketbe.inquiry.repository;

import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryMapper {
    void save(Inquiry inquiry);

    void deleteById(Long inquiryId);

    void deleteByCode(String inquiryCode);

    Inquiry findByInquiryId(Long inquiryId);

    Inquiry findByInquiryCode(String inquiryCode);

    List<Inquiry> findAll();
    List<Inquiry> findAllByUser(Long userId);

    List<Inquiry> findAllByPhone(String phone);

    void updateStatusByDel(Inquiry inquiry);

    void update(Inquiry inquiry);

    boolean existAnswerInquiry(Long inquiryId);
}

package com.cherrydev.cherrymarketbe.inquiry.repository;

import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryMapper {
    void save(Inquiry inquiry);

    void deleteById(Long inquiryId);

    Inquiry findByInquiryId(Long inquiryId);

    Inquiry findByInquiryCode(String inquiryCode);



//    @Select("SELECT * FROM notice WHERE notice_id = #{noticeId}
//    Notice findByNoticeId(Long noticeId);
//    Notice findByNoticeCode(String noticeCode);
//    List<Notice> findAll();
//
//    void delete(Long noticeId);
//
//    void update(Notice notice);
//
//    void updateStatus(Notice notice);

}

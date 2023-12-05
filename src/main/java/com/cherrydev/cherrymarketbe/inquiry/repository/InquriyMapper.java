package com.cherrydev.cherrymarketbe.inquiry.repository;

import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquriyMapper {
    void save(Inquiry inquiry);

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

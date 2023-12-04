package com.cherrydev.cherrymarketbe.notice.repository;

import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
    void insert(Notice notice);

    Notice getNoticeById(Long noticeId);
}

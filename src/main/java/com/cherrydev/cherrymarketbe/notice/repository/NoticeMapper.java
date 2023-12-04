package com.cherrydev.cherrymarketbe.notice.repository;

import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    void save(Notice notice);

    Notice findByNoticeId(Long noticeId);

    List<NoticeInfoDto> findAll();

    void delete(Long noticeId);
}

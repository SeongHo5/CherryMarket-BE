package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import com.cherrydev.cherrymarketbe.notice.repository.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정 추가
@RequiredArgsConstructor // 생성자 주입 추가

public class NoticeServiceImpl implements NoticeService{
    private final NoticeMapper noticeMapper;

    @Override
    @Transactional
    public void createNotice(final NoticeRequestDto noticeRequestDto) {
        noticeMapper.insert(noticeRequestDto.toEntity());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId) {
        Notice notice = noticeMapper.getNoticeById(noticeId);
        return ResponseEntity.ok().body(new NoticeInfoDto(notice)); // 응답 생성 및 반환
    }
}

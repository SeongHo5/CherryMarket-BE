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

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정 추가
@RequiredArgsConstructor // 생성자 주입 추가

public class NoticeServiceImpl implements NoticeService{
    private final NoticeMapper noticeMapper;

    @Override
    @Transactional
    public void createNotice(final NoticeRequestDto noticeRequestDto) {
        noticeMapper.save(noticeRequestDto.toEntity());
    }
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId) {
        Notice notice = noticeMapper.findByNoticeId(noticeId);
        return ResponseEntity.ok()
                .body(new NoticeInfoDto(notice)); // 응답 생성 및 반환
    }
    @Override
    @Transactional
    public ResponseEntity<List<NoticeInfoDto>> findAll() {
        List<NoticeInfoDto> notices = noticeMapper.findAll();
        return ResponseEntity.ok()
                .body(notices); // 응답 생성 및 반환{
                 // 응답 생성 및 반환
    }
    @Override
    @Transactional
    public void deleteNotice(final Long noticeId) {
        noticeMapper.delete(noticeId); // 삭제 처리
        log.info("Notice deleted with id: {}", noticeId); // 로그 출력
    }
}

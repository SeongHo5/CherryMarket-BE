package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;
import com.cherrydev.cherrymarketbe.notice.repository.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus.DELETED;

@Service
@Slf4j
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정 추가
@RequiredArgsConstructor // 생성자 주입 추가

public class NoticeServiceImpl implements NoticeService {
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
                .body(new NoticeInfoDto(notice));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(String noticeCode) {
        Notice notice = noticeMapper.findByNoticeCode(noticeCode);
        return ResponseEntity.ok()
                .body(new NoticeInfoDto(notice));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<NoticeInfoDto>> findAll() {

        List<NoticeInfoDto> notices = noticeMapper.findAll();
        return ResponseEntity.ok()
                .body(notices);
    }

    @Override
    @Transactional
    public void deleteNotice(final Long noticeId) {
        noticeMapper.delete(noticeId);
        log.info("Notice deleted with id: {}", noticeId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<NoticeInfoDto> modifyNotice(final ModifyNoticeInfoRequestDto modifyDto, final Long noticeId) {
//    public void modifyNotice(final ModifyNoticeInfoRequestDto modifyDto, final Long noticeId) {

        Notice notice = noticeMapper.findByNoticeId(noticeId);
        log.info("notice:getCode {}", notice.getCode());
        log.info("notice:getDisplayDate {}", notice.getDisplayDate());
        log.info("notice:getHideDate {}", notice.getHideDate());
        log.info("modifyDto:getDisplayDate {}", modifyDto.getDisplayDate());
        log.info("modifyDto:getHideDate {}", modifyDto.getHideDate());
//        NoticeInfoDto oldData = new NoticeInfoDto(notice);

        //기존의 상태만 DELETED 로 변경한다.
        notice.updateStatus(DELETED);
        log.info("notice:getStatus {}", notice.getStatus());
        noticeMapper.updateStatus(notice);

        //기존의 데이터를 NoticeInfoDto에 담아서 다시 insert 한다.
        Notice newNotice = Notice.builder()
                .code(notice.getCode())
                .category(NoticeCategory.valueOf(modifyDto.getCategory()))
                .subject(modifyDto.getSubject())
                .content(modifyDto.getContent())
                .status(DisplayStatus.ACTIVE)
                .displayDate(Timestamp.valueOf(modifyDto.getDisplayDate()))
                .hideDate(Timestamp.valueOf(modifyDto.getHideDate()))
                .deleteNt(null)
                .build();

        log.info("newNotice:displayDate {}", newNotice.getDisplayDate());
        log.info("newNotice:getHideDate {}", newNotice.getHideDate());
        log.info("newNotice:getDeleteNt {}", newNotice.getDeleteNt());
        noticeMapper.update(newNotice);

        NoticeInfoDto resultDto = new NoticeInfoDto(noticeMapper.findByNoticeId(newNotice.getNoticeId()));

        return ResponseEntity
                .ok()
                .body(resultDto);
    }


}


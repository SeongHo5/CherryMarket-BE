package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;
import com.cherrydev.cherrymarketbe.notice.repository.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;
import static com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus.DELETED;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
    public ResponseEntity<MyPage<NoticeInfoDto>> getAllNotice(final Pageable pageable) {
        MyPage<NoticeInfoDto> infoPage =  createPage(pageable, noticeMapper::findAll);
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }




    @Override
    @Transactional
    public void deleteById(final Long noticeId) {
        noticeMapper.deleteById(noticeId);
    }

    @Override
    @Transactional
    public void deleteByCode(final String code) {
        noticeMapper.deleteByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<NoticeInfoDto> modifyById(final ModifyNoticeInfoRequestDto modifyDto) {

        Notice notice = noticeMapper.findByNoticeId(modifyDto.getNoticeId());

        notice.updateStatus(DELETED);
        noticeMapper.updateStatus(notice);

        Notice newNotice = Notice.builder()
                .code(notice.getCode())
                .category(NoticeCategory.valueOf(modifyDto.getCategory()))
                .subject(modifyDto.getSubject())
                .content(modifyDto.getContent())
                .status(DisplayStatus.ACTIVE)
                .displayDate(Timestamp.valueOf(modifyDto.getDisplayDate()))
                .hideDate(Timestamp.valueOf(modifyDto.getHideDate()))
                .deleteDate(null)
                .build();

        noticeMapper.update(newNotice);
        NoticeInfoDto resultDto = new NoticeInfoDto(noticeMapper.findByNoticeId(newNotice.getNoticeId()));

        return ResponseEntity
                .ok()
                .body(resultDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<NoticeInfoDto> modifyByCode(final ModifyNoticeInfoRequestDto modifyDto) {

        Notice notice = noticeMapper.findByNoticeCode(modifyDto.getCode());

        notice.updateStatus(DELETED);
        noticeMapper.updateStatus(notice);

        Notice newNotice = Notice.builder()
                .code(modifyDto.getCode())
                .category(NoticeCategory.valueOf(modifyDto.getCategory()))
                .subject(modifyDto.getSubject())
                .content(modifyDto.getContent())
                .status(DisplayStatus.ACTIVE)
                .displayDate(Timestamp.valueOf(modifyDto.getDisplayDate()))
                .hideDate(Timestamp.valueOf(modifyDto.getHideDate()))
                .deleteDate(null)
                .build();

        noticeMapper.update(newNotice);
        NoticeInfoDto resultDto = new NoticeInfoDto(noticeMapper.findByNoticeCode(newNotice.getCode()));

        return ResponseEntity
                .ok()
                .body(resultDto);
    }

}


package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import com.cherrydev.cherrymarketbe.inquiry.repository.InquriyMapper;
import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;
import com.cherrydev.cherrymarketbe.notice.repository.NoticeMapper;
import com.cherrydev.cherrymarketbe.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus.DELETED;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class InquiryServiceImpl implements InquiryService {
    private final InquriyMapper inquriyMapper;

    @Override
    @Transactional
    public void createInquiry(final InquiryRequestDto inquiryRequestDto) {
        inquriyMapper.save(inquiryRequestDto.toEntity());
    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId) {
//        Notice notice = noticeMapper.findByNoticeId(noticeId);
//        return ResponseEntity.ok()
//                .body(new NoticeInfoDto(notice));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(String noticeCode) {
//        Notice notice = noticeMapper.findByNoticeCode(noticeCode);
//        return ResponseEntity.ok()
//                .body(new NoticeInfoDto(notice));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public ResponseEntity<List<NoticeInfoDto>> findAll() {
//        List<Notice> notices = noticeMapper.findAll();
//        List<NoticeInfoDto> noticeDtos = NoticeInfoDto.convertToDtoList(notices);
//        return ResponseEntity.ok()
//                .body(noticeDtos);
//    }
//
//    @Override
//    @Transactional
//    public void deleteNotice(final Long noticeId) {
//        noticeMapper.delete(noticeId);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ResponseEntity<NoticeInfoDto> modifyNotice(final ModifyNoticeInfoRequestDto modifyDto, final Long noticeId) {
//
//        Notice notice = noticeMapper.findByNoticeId(noticeId);
//
//        notice.updateStatus(DELETED);
//        noticeMapper.updateStatus(notice);
//
//        Notice newNotice = Notice.builder()
//                .code(notice.getCode())
//                .category(NoticeCategory.valueOf(modifyDto.getCategory()))
//                .subject(modifyDto.getSubject())
//                .content(modifyDto.getContent())
//                .status(DisplayStatus.ACTIVE)
//                .displayDate(Timestamp.valueOf(modifyDto.getDisplayDate()))
//                .hideDate(Timestamp.valueOf(modifyDto.getHideDate()))
//                .deleteDate(null)
//                .build();
//
//        noticeMapper.update(newNotice);
//        NoticeInfoDto resultDto = new NoticeInfoDto(noticeMapper.findByNoticeId(newNotice.getNoticeId()));
//
//        return ResponseEntity
//                .ok()
//                .body(resultDto);
//    }

}


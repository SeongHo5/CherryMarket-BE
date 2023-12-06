package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import com.cherrydev.cherrymarketbe.inquiry.repository.InquiryMapper;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class InquiryServiceImpl implements InquiryService {
    private final InquiryMapper inquiryMapper;

    @Override
    @Transactional
    public void createInquiry(final InquiryRequestDto inquiryRequestDto) {
        inquiryMapper.save(inquiryRequestDto.toEntity());
    }

    @Override
    @Transactional
    public void deleteInquiryById(final Long inquiryId) {
       inquiryMapper.deleteById(inquiryId);
    }



    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<InquiryInfoDto> getInquiryInfoById(final Long inquiryId) {
        Inquiry inquiry = inquiryMapper.findByInquiryId(inquiryId);
        return ResponseEntity.ok()
                .body(new InquiryInfoDto(inquiry));
    }

    @Override
    public ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode) {
        Inquiry inquiry = inquiryMapper.findByInquiryCode(inquiryCode);
        return ResponseEntity.ok()
                .body(new InquiryInfoDto(inquiry));
    }

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


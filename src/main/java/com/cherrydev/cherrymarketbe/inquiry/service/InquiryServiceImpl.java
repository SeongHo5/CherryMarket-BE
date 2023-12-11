package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.goodsReview.utils.BadWordFilter;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryDetailType;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryStatus;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryType;
import com.cherrydev.cherrymarketbe.inquiry.repository.InquiryMapper;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;
import static com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus.DELETED;

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
    @Transactional
    public void deleteInquiryByCode(String inquiryCode) {
        inquiryMapper.deleteByCode(inquiryCode);
    }

    @Override
    @Transactional
    public ResponseEntity<InquiryInfoDto> getInquiryInfoById(final Long inquiryId) {
        Inquiry inquiry = inquiryMapper.findByInquiryId(inquiryId);
        return getExistAnswer(inquiry);
    }


    @Override
    @Transactional
    public ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode) {
        Inquiry inquiry = inquiryMapper.findByInquiryCode(inquiryCode);
        return getExistAnswer(inquiry);
    }

    @Override
    @Transactional
    public ResponseEntity<MyPage<InquiryInfoDto>> findAll(final Pageable pageable) {

        List<InquiryInfoDto> getDto = inquiryMapper.findAll();
        getDto.forEach(dto -> dto.updateContent(CheckForForbiddenWordsTest(dto.getContent())));
        MyPage<InquiryInfoDto> infoPage = createPage(pageable, () -> getDto);

        return getListCheckAnswer(infoPage);
    }


    //    @Override
    @Transactional
    public ResponseEntity<MyPage<InquiryInfoDto>> findAllByUser(final Pageable pageable, final Long userId) {

        List<InquiryInfoDto> getDto = inquiryMapper.findAllByUser(userId);
        getDto.forEach(dto -> dto.updateContent(CheckForForbiddenWordsTest(dto.getContent())));
        MyPage<InquiryInfoDto> infoPage = createPage(pageable, () -> getDto);

        return getListCheckAnswer(infoPage);
    }

    @Override
    @Transactional
    public ResponseEntity<MyPage<InquiryInfoDto>> findAllByPhone(final Pageable pageable, final String phone) {

        List<InquiryInfoDto> getDto = inquiryMapper.findAllByPhone(phone);
        getDto.forEach(dto -> dto.updateContent(CheckForForbiddenWordsTest(dto.getContent())));
        MyPage<InquiryInfoDto> infoPage = createPage(pageable, () -> getDto);

        return getListCheckAnswer(infoPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<InquiryInfoDto> modifyInquiryById(final ModifyInquiryRequestDto modifyDto) {

        Inquiry inquiry = inquiryMapper.findByInquiryId(modifyDto.getInquiryId());

        inquiry.updateStatus(DELETED);
        inquiryMapper.updateStatusByDel(inquiry);

        Inquiry newInquiry = Inquiry.builder()
                .userId(inquiry.getUserId())
                .code(inquiry.getCode())
                .type(InquiryType.valueOf(modifyDto.getType()))
                .detailType(InquiryDetailType.valueOf(modifyDto.getDetailType()))
                .subject(modifyDto.getSubject())
                .content(modifyDto.getContent())
                .status(DisplayStatus.ACTIVE)
                .phone(inquiry.getPhone())
                .deleteDate(null)
                .build();

        inquiryMapper.update(newInquiry);
        InquiryInfoDto resultDto = new InquiryInfoDto(inquiryMapper.findByInquiryId(newInquiry.getInquiryId()));

        return ResponseEntity
                .ok()
                .body(resultDto);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<InquiryInfoDto> modifyInquiryByCode(final ModifyInquiryRequestDto modifyDto) {

        Inquiry inquiry = inquiryMapper.findByInquiryCode(modifyDto.getCode());


        inquiry.updateStatus(DELETED);
        inquiryMapper.updateStatusByDel(inquiry);

        Inquiry newInquiry = Inquiry.builder()
                .userId(inquiry.getUserId())
                .code(modifyDto.getCode())
                .type(InquiryType.valueOf(modifyDto.getType()))
                .detailType(InquiryDetailType.valueOf(modifyDto.getDetailType()))
                .subject(modifyDto.getSubject())
                .content(modifyDto.getContent())
                .status(DisplayStatus.ACTIVE)
                .phone(inquiry.getPhone())
                .deleteDate(null)
                .build();

        inquiryMapper.update(newInquiry);
        InquiryInfoDto resultDto = new InquiryInfoDto(inquiryMapper.findByInquiryCode(newInquiry.getCode()));

        return ResponseEntity
                .ok()
                .body(resultDto);
    }

    // =============== PRIVATE METHODS =============== //

    /**
     * 1:1문의 사항의 답변이 작성되어 있는지 확인한다.
     */
    private ResponseEntity<InquiryInfoDto> getExistAnswer(Inquiry inquiry) {
        if (inquiryMapper.existAnswerInquiry(inquiry.getInquiryId())) {
            inquiry.updateAnswerStatus(InquiryStatus.COMPLETE);
            inquiryMapper.updateAnswerStatus(inquiry);
        } else {
            inquiry.updateAnswerStatus(InquiryStatus.ACCEPTING);
            inquiryMapper.updateAnswerStatus(inquiry);
        }
        return ResponseEntity.ok()
                .body(new InquiryInfoDto(inquiry));
    }

    private ResponseEntity<MyPage<InquiryInfoDto>> getListCheckAnswer(MyPage<InquiryInfoDto> infoPage) {
        List<InquiryInfoDto> inquiries = infoPage.getContent();

        for (InquiryInfoDto inquiry : inquiries) {
            if (inquiryMapper.existAnswerInquiry(inquiry.getInquiryId())) {
                inquiry.updateAnswerStatus(InquiryStatus.COMPLETE);
                inquiryMapper.updateAnswerStatusByInfo(inquiry);
            } else {
                inquiry.updateAnswerStatus(InquiryStatus.ACCEPTING);
                inquiryMapper.updateAnswerStatusByInfo(inquiry);
            }
        }

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    private String CheckForForbiddenWordsTest(String content) {
        System.out.println("CheckForForbiddenWords start");
        return BadWordFilter.filterAndReplace(content);
    }
}


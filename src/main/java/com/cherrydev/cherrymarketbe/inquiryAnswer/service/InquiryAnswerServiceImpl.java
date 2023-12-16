package com.cherrydev.cherrymarketbe.inquiryAnswer.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnswerInfoDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.entity.InquiryAnswer;
import com.cherrydev.cherrymarketbe.inquiryAnswer.repository.InquiryAnswerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_ANSWER;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_ALLOWED_EMPTY_CONTENT;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryAnswerServiceImpl implements InquiryAnswerService {
    private final InquiryAnswerMapper inquiryAnswerMapper;

    @Override
    @Transactional
    public void createInquiryAnswer(final InquiryAnwerRequestDto inquiryAnwerRequestDto) {
        CheckValidationForInsert(inquiryAnwerRequestDto);
        inquiryAnswerMapper.save(inquiryAnwerRequestDto.toEntity());
    }

    @Override
    @Transactional
    public void updateInquirStatus(final InquiryAnwerRequestDto inquiryAnwerRequestDto) {
        inquiryAnswerMapper.updateStatue(inquiryAnwerRequestDto.getInquiryId());
    }

    @Override
    @Transactional
    public ResponseEntity<InquiryAnswerInfoDto> getInquiryAnswerById(Long inquiryId) {
        InquiryAnswer inquiryAnswer = inquiryAnswerMapper.findById(inquiryId);
        return ResponseEntity.ok()
                .body(new InquiryAnswerInfoDto(inquiryAnswer));
    }

    @Override
    @Transactional
    public ResponseEntity<MyPage<InquiryAnswerInfoDto>> findAll(final Pageable pageable) {
        MyPage<InquiryAnswerInfoDto> infoPage =  createPage(pageable, inquiryAnswerMapper::findAll);
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional
    public ResponseEntity<MyPage<InquiryAnswerInfoDto>> getAnswerByUserId(final Pageable pageable, Long userId) {
        MyPage<InquiryAnswerInfoDto> infoPage =  createPage(pageable, () -> inquiryAnswerMapper.findByUserId(userId));
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional
    public ResponseEntity<MyPage<InquiryAnswerInfoDto>> getAnswerByEmail(final Pageable pageable,String email) {
        MyPage<InquiryAnswerInfoDto> infoPage =  createPage(pageable, () -> inquiryAnswerMapper.findByEmail(email));
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional
    public void deleteAndSetStatusAccepting(Long answerId) {
        try {
            inquiryAnswerMapper.updateStatusAccepting(answerId);
            inquiryAnswerMapper.delete(answerId);
        } catch (Exception e) {
            throw new RuntimeException("DeleteAndSetStatusAccepting Transaction failed", e);
        }
    }


    // =============== PRIVATE METHODS =============== //

    private void CheckValidationForInsert(InquiryAnwerRequestDto inquiryAnwerRequestDto) {
        if (inquiryAnwerRequestDto.getContent() == null || inquiryAnwerRequestDto.getContent().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_CONTENT);
        }
        isDuplicate(inquiryAnwerRequestDto.getInquiryId());
    }

    private void isDuplicate(Long inquiryId){
        if (inquiryAnswerMapper.existAnswer(inquiryId)){
            throw new ServiceFailedException(ALREADY_EXIST_ANSWER);
        }
    }
}

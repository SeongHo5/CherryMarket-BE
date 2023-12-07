package com.cherrydev.cherrymarketbe.inquiryAnswer.service;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnswerInfoDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.entity.InquiryAnswer;
import com.cherrydev.cherrymarketbe.inquiryAnswer.repository.InquiryAnswerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryAnswerServiceImpl implements InquiryAnswerService{
    private final InquiryAnswerMapper  inquiryAnswerMapper;

    @Override
    @Transactional
    public void createInquiryAnswer(final InquiryAnwerRequestDto inquiryAnwerRequestDto) {
        inquiryAnswerMapper.save(inquiryAnwerRequestDto.toEntity());
    }

    @Override
    @Transactional
    public void updateInquirStatus(final InquiryAnwerRequestDto inquiryAnwerRequestDto){
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
    public ResponseEntity<List<InquiryAnswerInfoDto>> findAll() {
        List<InquiryAnswer> inquiryAnswer = inquiryAnswerMapper.findAll();
        List<InquiryAnswerInfoDto> inquiryAnswerInfoDtos = InquiryAnswerInfoDto.convertToDtoList(inquiryAnswer);
        return ResponseEntity.ok()
                .body(inquiryAnswerInfoDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<List<InquiryAnswerInfoDto>> getAnswerByUserId(Long userId) {
        List<InquiryAnswer> inquiryAnswer = inquiryAnswerMapper.findByUserId(userId);
        List<InquiryAnswerInfoDto> inquiryAnswerInfoDtos = InquiryAnswerInfoDto.convertToDtoList(inquiryAnswer);
        return ResponseEntity.ok()
                .body(inquiryAnswerInfoDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<List<InquiryAnswerInfoDto>> getAnswerByEmail(String email) {
        List<InquiryAnswer> inquiryAnswer = inquiryAnswerMapper.findByEmail(email);
        List<InquiryAnswerInfoDto> inquiryAnswerInfoDtos = InquiryAnswerInfoDto.convertToDtoList(inquiryAnswer);
        return ResponseEntity.ok()
                .body(inquiryAnswerInfoDtos);
    }




}

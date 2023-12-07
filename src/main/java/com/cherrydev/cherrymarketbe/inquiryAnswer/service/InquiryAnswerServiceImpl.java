package com.cherrydev.cherrymarketbe.inquiryAnswer.service;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.repository.InquiryAnswerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}

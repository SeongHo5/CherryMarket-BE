package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.entity.Inquiry;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryDetailType;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryType;
import com.cherrydev.cherrymarketbe.inquiry.repository.InquiryMapper;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional(readOnly = true)
    public ResponseEntity<InquiryInfoDto> getInquiryInfoById(final Long inquiryId) {
        Inquiry inquiry = inquiryMapper.findByInquiryId(inquiryId);
        return ResponseEntity.ok()
                .body(new InquiryInfoDto(inquiry));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode) {
        Inquiry inquiry = inquiryMapper.findByInquiryCode(inquiryCode);
        return ResponseEntity.ok()
                .body(new InquiryInfoDto(inquiry));
    }




    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<InquiryInfoDto>> findAll() {
        List<Inquiry> inquiries = inquiryMapper.findAll();
        List<InquiryInfoDto> inquiryInfoDtos = InquiryInfoDto.convertToDtoList(inquiries);
        return ResponseEntity.ok()
                .body(inquiryInfoDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<InquiryInfoDto>> findAllByUser(final Long userId) {
        List<Inquiry> inquiries = inquiryMapper.findAllByUser(userId);
        List<InquiryInfoDto> inquiryInfoDtos = InquiryInfoDto.convertToDtoList(inquiries);
        return ResponseEntity.ok()
                .body(inquiryInfoDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<InquiryInfoDto>> findAllByPhone(String phone) {
        List<Inquiry> inquiries = inquiryMapper.findAllByPhone(phone);
        List<InquiryInfoDto> inquiryInfoDtos = InquiryInfoDto.convertToDtoList(inquiries);
        return ResponseEntity.ok()
                .body(inquiryInfoDtos);
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

    private void checkAnswerInquiryExist(final Long inquiryId) {
        inquiryMapper.existAnswerInquiry(inquiryId);
    }
}


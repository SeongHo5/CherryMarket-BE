package com.cherrydev.cherrymarketbe.inquiry.controller;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.service.InquiryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryServiceImpl inquiryService;

    // ==================== INSERT ==================== //
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addInquiry(final @Valid @RequestBody InquiryRequestDto inquiryRequestDto) {
        inquiryService.createInquiry(inquiryRequestDto);
    }

    // ==================== SELECT ==================== //
    // By 아이디
    @GetMapping("/search-id")
    public ResponseEntity<InquiryInfoDto> getNoticeInfoById(@RequestParam Long inquiryId) {
        return inquiryService.getInquiryInfoById(inquiryId);
    }

    // By 코드
    @GetMapping("/search-code")
    public ResponseEntity<InquiryInfoDto> getNoticeInfoByCode(@RequestParam String inquiryCode) {
        return inquiryService.getInquiryInfoByCode(inquiryCode);
    }

    //By 회원 아이디
    @GetMapping("/list/user")
    public ResponseEntity<MyPage<InquiryInfoDto>> getInquiryListByUser(@RequestParam Long userId, final Pageable pageable) {
        return inquiryService.findAllByUser(pageable, userId);
    }


    //By 회원 핸드폰 번호
    @GetMapping("/list/phone")
    public ResponseEntity<MyPage<InquiryInfoDto>> getInquiryListByPhone(@RequestParam String phone, final Pageable pageable) {
        return inquiryService.findAllByPhone(pageable, phone);
    }

    // 전체 조회
    @GetMapping("/list")
    public ResponseEntity<MyPage<InquiryInfoDto>> getInquiryList(final Pageable pageable) {
        return inquiryService.findAll(pageable);
    }


    // ==================== UPDATE ==================== //
    // By 아이디
    @PatchMapping("/modify-id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InquiryInfoDto> modifyInquiryById(
            final @RequestBody ModifyInquiryRequestDto requestDto) {
        return inquiryService.modifyInquiryById(requestDto);
    }

    // By 코드
    @PatchMapping("/modify-code")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InquiryInfoDto> modifyInquiryByCode(
            final @RequestBody ModifyInquiryRequestDto requestDto) {
        return inquiryService.modifyInquiryByCode(requestDto);
    }

    // ==================== DELETE ==================== //

    // By 아이디
    @DeleteMapping("/delete/id")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam Long inquiryId) {
        inquiryService.deleteInquiryById(inquiryId);
    }

    // By 코드
    @DeleteMapping("/delete/code")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam String inquiryCode) {
        inquiryService.deleteInquiryByCode(inquiryCode);
    }
}

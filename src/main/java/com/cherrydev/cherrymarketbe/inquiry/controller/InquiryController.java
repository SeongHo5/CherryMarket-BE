package com.cherrydev.cherrymarketbe.inquiry.controller;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.service.InquiryServiceImpl;
import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.service.NoticeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryServiceImpl inquiryService;

    // 등록
    @PostMapping("/add-inquiry")
    @ResponseStatus(HttpStatus.CREATED)
    public void addInquiry(final @Valid @RequestBody InquiryRequestDto inquiryRequestDto) {
        inquiryService.createInquiry(inquiryRequestDto);
    }

    // --------조회--------
    // By 아이디
    @GetMapping("/info-inquiry/search-id")
    public ResponseEntity<InquiryInfoDto> getNoticeInfoById(@RequestParam Long inquiryId) {
        return inquiryService.getInquiryInfoById(inquiryId);
    }

    // By 코드
    @GetMapping("/info-inquiry/search-code")
    public ResponseEntity<InquiryInfoDto> getNoticeInfoByCode(@RequestParam String inquiryCode) {

        return inquiryService.getInquiryInfoByCode(inquiryCode);
    }

     //By 회원 아이디
    @GetMapping("/inquiry-list/user")
    public ResponseEntity<List<InquiryInfoDto>> getInquiryListByUser(@RequestParam Long userId) {
        return inquiryService.findAllByUser(userId);
    }

    //By 회원 핸드폰 번호
    @GetMapping("/inquiry-list/phone")
    public ResponseEntity<List<InquiryInfoDto>> getInquiryListByPhone(@RequestParam String phone) {
        return inquiryService.findAllByPhone(phone);
    }
    // 전체 조회
    @GetMapping("/inquiry-list")
    public ResponseEntity<List<InquiryInfoDto>> getInquiryList() {
        return inquiryService.findAll();
    }


    // -----수정-------
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

    // -----------삭제----------------
    // By 아이디
    @DeleteMapping("/delete-inquiry-id")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam Long inquiryId) {
        inquiryService.deleteInquiryById(inquiryId);
    }

    // By 코드
    @DeleteMapping("/delete-inquiry-code")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam String  inquiryCode) {
        inquiryService.deleteInquiryByCode(inquiryCode);
    }
}

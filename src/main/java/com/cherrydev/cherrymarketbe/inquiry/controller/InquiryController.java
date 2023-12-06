package com.cherrydev.cherrymarketbe.inquiry.controller;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
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


    // 조회 - 아이디
    @GetMapping("/info-inquiry/search-id")
    public ResponseEntity<InquiryInfoDto> getNoticeInfoById(@RequestParam Long inquiryId) {
        return inquiryService.getInquiryInfoById(inquiryId);
    }

    // 조회 - 코드
    @GetMapping("/info-inquiry/search-code")
    public ResponseEntity<InquiryInfoDto> getNoticeInfoByCode(@RequestParam String inquiryCode) {

        return inquiryService.getInquiryInfoByCode(inquiryCode);
    }
//
//    // 전체 조회
//    @GetMapping("/notice-list")
//    public ResponseEntity<List<NoticeInfoDto>> getNoticeList() {
//        return noticeService.findAll();
//    }
//
//    // 수정
//    @PatchMapping("/modify")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<NoticeInfoDto> modifyNotice(
//            final @RequestBody ModifyNoticeInfoRequestDto requestDto,
//            @RequestParam Long noticeId) {
//      return noticeService.modifyNotice(requestDto, noticeId);
//    }

    // 삭제
    @DeleteMapping("/delete-inquiryId")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam Long inquiryId) {
        inquiryService.deleteInquiryById(inquiryId);
    }
}

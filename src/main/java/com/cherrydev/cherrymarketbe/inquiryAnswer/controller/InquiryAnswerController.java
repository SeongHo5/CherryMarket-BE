package com.cherrydev.cherrymarketbe.inquiryAnswer.controller;

import com.cherrydev.cherrymarketbe.inquiry.service.InquiryServiceImpl;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.service.InquiryAnswerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiryAnswer")
public class InquiryAnswerController {

    private final InquiryAnswerServiceImpl inquiryAnswerService;

    // ==================== INSERT ==================== //
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addInquiry(final @Valid @RequestBody InquiryAnwerRequestDto inquiryAnwerRequestDto) {
        inquiryAnswerService.createInquiryAnswer(inquiryAnwerRequestDto);
        inquiryAnswerService.updateInquirStatus(inquiryAnwerRequestDto);
    }
//
//    // ==================== SELECT ==================== //
//    // By 아이디
//    @GetMapping("/info-InquiryAnswer/search-id")
//    public ResponseEntity<InquiryInfoDto> getNoticeInfoById(@RequestParam Long inquiryId) {
//        return inquiryAnswerService.getInquiryAnswerInfoById(inquiryId);
//    }
//
//    // ==================== DELETE ==================== //
//
//    // By 아이디
//    @DeleteMapping("/delete-InquiryAnswer-id")
//    @ResponseStatus(HttpStatus.OK)
//    public void deleteInquiryById(@RequestParam Long inquiryId) {
//        inquiryAnswerService.deleteInquiryAnswerById(inquiryId);
//    }
}

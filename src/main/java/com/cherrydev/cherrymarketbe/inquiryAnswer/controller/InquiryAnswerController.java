package com.cherrydev.cherrymarketbe.inquiryAnswer.controller;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnswerInfoDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.service.InquiryAnswerServiceImpl;
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

    //    // ==================== SELECT ==================== //
    // By 아이디
    @GetMapping("/search-answerId")
    public ResponseEntity<InquiryAnswerInfoDto> getAnswerById(@RequestParam Long inquiryId) {
        return inquiryAnswerService.getInquiryAnswerById(inquiryId);
    }

    @GetMapping("/search-list")
    public ResponseEntity<MyPage<InquiryAnswerInfoDto>> getAnswerList(final Pageable pageable) {
        return inquiryAnswerService.findAll(pageable);
    }

    @GetMapping("/search-userId")
    public ResponseEntity<MyPage<InquiryAnswerInfoDto>> getAnswerByUserId(final Pageable pageable, @RequestParam Long userId) {
        return inquiryAnswerService.getAnswerByUserId(pageable,userId);
    }

    //유저 이메일로 검색
    @GetMapping("/search-email")
    public ResponseEntity<MyPage<InquiryAnswerInfoDto>> getAnswerByEmail(final Pageable pageable,@RequestParam String email) {
        return inquiryAnswerService.getAnswerByEmail(pageable,email);
    }

    // ==================== DELETE ==================== //

    // By 아이디
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAndSetStatusAccepting(@RequestParam Long answerId) {
        inquiryAnswerService.deleteAndSetStatusAccepting(answerId);
    }
}

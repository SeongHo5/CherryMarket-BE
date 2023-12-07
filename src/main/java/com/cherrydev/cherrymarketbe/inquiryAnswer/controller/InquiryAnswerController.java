package com.cherrydev.cherrymarketbe.inquiryAnswer.controller;

import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnswerInfoDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.service.InquiryAnswerServiceImpl;
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
    public ResponseEntity<List<InquiryAnswerInfoDto>> getAnswerList() {
        return inquiryAnswerService.findAll();
    }
     @GetMapping("/search-userId")
    public ResponseEntity<List<InquiryAnswerInfoDto>> getAnswerByUserId(@RequestParam Long userId) {
        return inquiryAnswerService.getAnswerByUserId(userId);
    }
    //유저 이메일로 검색
     @GetMapping("/search-email")
    public ResponseEntity<List<InquiryAnswerInfoDto>> getAnswerByEmail(@RequestParam String email) {
        return inquiryAnswerService.getAnswerByEmail(email);
    }



    //전체 조회



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

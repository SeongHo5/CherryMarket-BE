package com.cherrydev.cherrymarketbe.inquiry.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.service.FileService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryServiceImpl inquiryService;
    private final FileService fileService;

    // ==================== INSERT ==================== //
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addInquiry(final @Valid @RequestBody InquiryRequestDto inquiryRequestDto, final @AuthenticationPrincipal AccountDetails accountDetails) {
        inquiryService.createInquiry(inquiryRequestDto, accountDetails);
    }


    @PostMapping("/add-image")
    @ResponseStatus(HttpStatus.CREATED)
    public void addImage(@RequestPart("imageFiles") List<MultipartFile> imageFiles, String dirName) {
        fileService.uploadMultipleFiles(imageFiles, dirName);
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

    //내 문의 전체 조회
    @GetMapping("/list/my")
    public ResponseEntity<MyPage<InquiryInfoDto>> getMyInquiryList(final Pageable pageable, final @AuthenticationPrincipal AccountDetails accountDetails) {
        return inquiryService.findAllMyList(pageable, accountDetails.getAccount().getAccountId());
    }


    // ==================== UPDATE ==================== //
    // By 아이디
    @PatchMapping("/modify-id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InquiryInfoDto> modifyInquiryById(
            final @RequestBody ModifyInquiryRequestDto requestDto, final @AuthenticationPrincipal AccountDetails accountDetails) {
        return inquiryService.modifyInquiryById(requestDto,accountDetails);
    }

    // By 코드
    @PatchMapping("/modify-code")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InquiryInfoDto> modifyInquiryByCode(
            final @RequestBody ModifyInquiryRequestDto requestDto, final @AuthenticationPrincipal AccountDetails accountDetails) {
        return inquiryService.modifyInquiryByCode(requestDto, accountDetails);
    }

    // ==================== DELETE ==================== //

    // By 아이디
    @DeleteMapping("/delete/id")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam Long inquiryId, final @AuthenticationPrincipal AccountDetails accountDetails) {
        inquiryService.deleteInquiryById(inquiryId, accountDetails);
    }

    // By 코드
    @DeleteMapping("/delete/code")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam String inquiryCode, final @AuthenticationPrincipal AccountDetails accountDetails) {
        inquiryService.deleteInquiryByCode(inquiryCode, accountDetails);
    }
}

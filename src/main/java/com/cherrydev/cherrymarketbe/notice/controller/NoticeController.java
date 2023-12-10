package com.cherrydev.cherrymarketbe.notice.controller;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.service.NoticeServiceImpl;
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
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    // 등록
    @PostMapping("/add-notice")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNotice(final @Valid @RequestBody NoticeRequestDto noticeRequestDto) {
        noticeService.createNotice(noticeRequestDto);
    }

    // 조회 - 아이디
    @GetMapping("/notice-info/search-id")
    public ResponseEntity<NoticeInfoDto> getNoticeInfoById(@RequestParam Long noticeId) {
        return noticeService.getNoticeInfo(noticeId);
    }

    // 조회 - 코드
    @GetMapping("/notice-info/search-code")
    public ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(@RequestParam String noticeCode) {

        return noticeService.getNoticeInfoByCode(noticeCode);
    }


    // 전체 조회
    @GetMapping("/notice-list")
    public ResponseEntity<MyPage<NoticeInfoDto>> getNoticeList(final Pageable pageable) {
        return noticeService.getAllNotice(pageable);
    }

    // 수정 - 아이디
    @PatchMapping("/modify-id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NoticeInfoDto> modifyById(
            final @RequestBody ModifyNoticeInfoRequestDto requestDto) {
        return noticeService.modifyById(requestDto);
    }

    // 수정 - 코드
    @PatchMapping("/modify-code")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NoticeInfoDto> modifyByCode(
            final @RequestBody ModifyNoticeInfoRequestDto requestDto) {
        return noticeService.modifyByCode(requestDto);
    }

    // 삭제 - 아이디
    @DeleteMapping("/delete-notice-id")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@RequestParam Long noticeId) {
        noticeService.deleteById(noticeId);
    }

    // 삭제 - 코드
    @DeleteMapping("/delete-notice-code")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByCode(@RequestParam String code) {
        noticeService.deleteByCode(code);
    }
}

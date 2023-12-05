package com.cherrydev.cherrymarketbe.notice.controller;

import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
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
    public ResponseEntity<List<NoticeInfoDto>> getNoticeList() {
        return noticeService.findAll();
    }

    // 수정
    @PatchMapping("/modify")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NoticeInfoDto> modifyNotice(
            final @RequestBody ModifyNoticeInfoRequestDto requestDto,
            @RequestParam Long noticeId) {
      return noticeService.modifyNotice(requestDto, noticeId);
    }

    // 삭제 - 아이디
    @DeleteMapping("/delete-notice")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNotice(@RequestParam Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}

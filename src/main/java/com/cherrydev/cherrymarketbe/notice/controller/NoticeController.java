package com.cherrydev.cherrymarketbe.notice.controller;

import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.service.NoticeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    //공지사항 등록
    @PostMapping("/add-notice")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNotice(final @Valid @RequestBody NoticeRequestDto noticeRequestDto){
        noticeService.createNotice(noticeRequestDto);
 }
    //공지사항 수정
    @GetMapping("/notice-info")
    public ResponseEntity<NoticeInfoDto> getNoticeInfo(@RequestParam Long noticeId) {
        return noticeService.getNoticeInfo(noticeId);
    }

}

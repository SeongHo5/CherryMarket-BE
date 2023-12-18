package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;

import java.sql.Timestamp;

public class NoticeFactory {


    //등록 성공
    public static NoticeRequestDto createNoticeA() {
        return NoticeRequestDto.builder()
                .category("EVENT")
                .subject("공지사항 테스트 1")
                .content("공지사항 등록 테스트 실시합니다.")
                .status(DisplayStatus.ACTIVE.toString())
                .displayDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .hideDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .build();
    }

    //카테고리 누락
    public static NoticeRequestDto createNoticeB() {
        return NoticeRequestDto.builder()
                .category("")
                .subject("공지사항 테스트 1")
                .content("공지사항 등록 테스트 실시합니다.")
                .status(DisplayStatus.ACTIVE.toString())
                .displayDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .hideDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .build();
    }

    //제목 누락
    public static NoticeRequestDto createNoticeC() {
        return NoticeRequestDto.builder()
                .category("EVENT")
                .subject("")
                .content("공지사항 등록 테스트 실시합니다.")
                .status(DisplayStatus.ACTIVE.toString())
                .displayDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .hideDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .build();
    }

    //내용 누락
    public static NoticeRequestDto createNoticeD() {
        return NoticeRequestDto.builder()
                .category("EVENT")
                .subject("공지사항 테스트 1")
                .content("")
                .status(DisplayStatus.ACTIVE.toString())
                .displayDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .hideDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .build();
    }

    //게시날짜 누락
    public static NoticeRequestDto createNoticeE() {
        return NoticeRequestDto.builder()
                .category("EVENT")
                .subject("공지사항 테스트 1")
                .content("")
                .status(DisplayStatus.ACTIVE.toString())
                .displayDate(null)
                .hideDate(null)
                .build();
    }

    //공지사항 수정 - 아이디
    public static ModifyNoticeInfoRequestDto createModifyNoticeInfoA() {
        return ModifyNoticeInfoRequestDto.builder()
                .noticeId(248L)
                .category(NoticeCategory.ANNOUNCEMENT.toString())
                .subject("공지사항 테스트 입니다.")
                .content("테스트 시작")
                .displayDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .hideDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .build();
    }

    public static ModifyNoticeInfoRequestDto createModifyNoticeInfoB() {
        return ModifyNoticeInfoRequestDto.builder()
                .code("NT39")
                .category(NoticeCategory.ANNOUNCEMENT.toString())
                .subject("공지사항 테스트 입니다.")
                .content("테스트 시작")
                .displayDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .hideDate(Timestamp.valueOf("2023-12-05 00:00:00").toString())
                .build();
    }



}

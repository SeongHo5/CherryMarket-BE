package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryDetailType;
import com.cherrydev.cherrymarketbe.inquiry.enums.InquiryType;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;

public class InquiryFactory {


    //등록 성공
    public static InquiryRequestDto createInquiryA() {
        return InquiryRequestDto.builder()
//                .userId(137L)
                .code("001")
                .type(InquiryType.ORDER.toString())
                .detailType(InquiryDetailType.INFO_CHANGE.toString())
                .subject("정보 변경건으로 문의 드립니다.")
                .content("배송지 변경합니다.")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //카테고리 누락
    public static InquiryRequestDto createInquiryB() {
        return InquiryRequestDto.builder()
//                .userId(137L)
                .code("001")
                .type(null)
                .detailType(InquiryDetailType.INFO_CHANGE.toString())
                .subject("정보 변경건으로 문의 드립니다.")
                .content("배송지 변경합니다.")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //세부카테고리 누락
    public static InquiryRequestDto createInquiryC() {
        return InquiryRequestDto.builder()
//                .userId(137L)
                .code("001")
                .type(InquiryType.ORDER.toString())
                .detailType(null)
                .subject("정보 변경건으로 문의 드립니다.")
                .content("배송지 변경합니다.")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //제목 누락
    public static InquiryRequestDto createInquiryD() {
        return InquiryRequestDto.builder()
//                .userId(137L)
                .code("001")
                .type(InquiryType.ORDER.toString())
                .detailType(InquiryDetailType.INFO_CHANGE.toString())
                .subject("")
                .content("배송지 변경합니다.")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //내용 누락
    public static InquiryRequestDto createInquiryE() {
        return InquiryRequestDto.builder()
//                .userId(137L)
                .code("001")
                .type(InquiryType.ORDER.toString())
                .detailType(InquiryDetailType.INFO_CHANGE.toString())
                .subject("정보 변경건으로 문의 드립니다.")
                .content("")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //수정 아이디
    public static ModifyInquiryRequestDto createModifyInquiryA() {
        return ModifyInquiryRequestDto.builder()
                .inquiryId(82L)
    			.type(InquiryType.ORDER.toString())
    			.detailType(InquiryDetailType.INFO_CHANGE.toString())
    			.subject("데이터 수정 테스트")
    			.content("수정 테스트 ")
    			.build();
    }

    //수정 코드
    public static ModifyInquiryRequestDto createModifyInquiryB() {
        return ModifyInquiryRequestDto.builder()
                .code("INQ11")
                .type(InquiryType.ORDER.toString())
                .detailType(InquiryDetailType.INFO_CHANGE.toString())
                .subject("데이터 수정 테스트")
                .content("수정 테스트 ")
                .build();
    }
}

package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.goodsreview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsreview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsreview.enums.GoodsReviewBest;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


public class GoodsReviewFactory {


    //등록 성공
    public static GoodsReviewRequestDto createGoodsReviewRequestDtoA() {
        return GoodsReviewRequestDto.builder()
                .ordersId(126L)
                .goodsId(22L)
                .code("1")
                .isBest(GoodsReviewBest.NORMAL.toString())
                .subject("키보드 받침대 사용후기")
                .content("1달 사용 후기입니다. 생각보다 괜찮고 가격대비 품질이 나쁘지 않아요")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //제목 누락
    public static GoodsReviewRequestDto createGoodsReviewRequestDtoB() {
        return GoodsReviewRequestDto.builder()
                .ordersId(126L)
                .goodsId(44L)
                .code("1")
                .isBest(GoodsReviewBest.NORMAL.toString())
                .subject("")
                .content("1달 사용 후기입니다. 생각보다 괜찮고 가격대비 품질이 나쁘지 않아요")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //내용 누락
    public static GoodsReviewRequestDto createGoodsReviewRequestDtoC() {
        return GoodsReviewRequestDto.builder()
                .ordersId(126L)
                .goodsId(44L)
                .code("1")
                .isBest(GoodsReviewBest.NORMAL.toString())
                .subject("키보드 받침대 사용후기")
                .content("")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }


    //중복 등록 실패
    public static GoodsReviewRequestDto createGoodsReviewRequestDtoD() {
        return GoodsReviewRequestDto.builder()
                .ordersId(122L)
                .goodsId(10L)
                .code("1")
                .isBest(GoodsReviewBest.NORMAL.toString())
                .subject("키보드 받침대 사용후기")
                .content("1달 사용 후기입니다. 생각보다 괜찮고 가격대비 품질이 나쁘지 않아요")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //배송상태 실패
    public static GoodsReviewRequestDto createGoodsReviewRequestDtoE() {
        return GoodsReviewRequestDto.builder()
                .ordersId(125L)
                .goodsId(15L)
                .code("1")
                .isBest(GoodsReviewBest.NORMAL.toString())
                .subject("키보드 받침대 사용후기")
                .content("1달 사용 후기입니다. 생각보다 괜찮고 가격대비 품질이 나쁘지 않아요")
                .status(DisplayStatus.ACTIVE.toString())
                .build();
    }

    //수정
    public static GoodsReviewModifyDto createGoodsReviewModifyDto() {
        return GoodsReviewModifyDto.builder()
                .ordersId(122L)
                .goodsId(20L)
                .subject("테스트 수정합니다")
                .content("테스트 합니다.")
                .build();
    }


    // 파일 업로드 테스트용 더미 데이터
    public static MultipartFile createDummyMultipartFile(String originalFileName, String contentType, byte[] content) {
        return new MockMultipartFile(originalFileName, originalFileName, contentType, content);
    }

}

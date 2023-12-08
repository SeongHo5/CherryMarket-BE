package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsReview.repository.GoodsReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_REVIEW;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.REVIEW_NOT_ALLOWED_BEFORE_DELIVERY;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsReviewServiceImpl implements GoodsReviewService {

    private final GoodsReviewMapper goodsReviewMapper;

    @Override
    @Transactional
    public void save(final GoodsReviewRequestDto goodsReviewRequestDto) {
        CheckExistReview(goodsReviewRequestDto.toEntity());
        CheckDelieveryStatus(goodsReviewRequestDto.toEntity());
        goodsReviewMapper.save(goodsReviewRequestDto.toEntity());
    }

    @Override
    @Transactional
    public void update() {

    }

    @Override
    @Transactional
    public void delete() {

    }

    @Override
    @Transactional
    public ResponseEntity<GoodsReviewInfoDto> getReview(final Long ordersId, Long goodsId) {
        GoodsReview goodsReview = goodsReviewMapper.findReivew(ordersId, goodsId);
        return ResponseEntity.ok()
                .body(new GoodsReviewInfoDto(goodsReview));
    }

    @Override
    @Transactional
    public void getReviewList() {

    }

    @Override
    @Transactional
    public void getGoodsReviewListByGoodsId() {

    }

    @Override
    @Transactional
    public void getGoodsReviewListByUserId() {

    }


    // =============== PRIVATE METHODS =============== //

    private void CheckExistReview(GoodsReview goodsReview) {
        if (goodsReviewMapper.existReview(goodsReview)) {
            throw new DuplicatedException(ALREADY_EXIST_REVIEW);
        }
    }

    private void CheckDelieveryStatus(GoodsReview goodsReview) {
        if (!goodsReviewMapper.checkDeliveryStatus(goodsReview)) {
            throw new ServiceFailedException(REVIEW_NOT_ALLOWED_BEFORE_DELIVERY);
        }
    }
}

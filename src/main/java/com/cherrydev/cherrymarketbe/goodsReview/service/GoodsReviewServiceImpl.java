package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.goods.repository.GoodsMapper;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsReview.repository.GoodsReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_REVIEW;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.CONFLICT_ACCOUNT;

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
    public void getReview() {

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
        ;
    }
}

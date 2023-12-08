package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.goods.repository.GoodsMapper;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.repository.GoodsReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsReviewServiceImpl implements GoodsReviewService {

    private final GoodsReviewMapper goodsReviewMapper;

    @Override
    @Transactional
    public void save(final GoodsReviewRequestDto goodsReviewRequestDto) {
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

}

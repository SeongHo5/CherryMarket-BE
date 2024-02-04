package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_GOODS;
import static com.cherrydev.cherrymarketbe.server.domain.goods.enums.SalesStatus.ON_SALE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    public static final String DISCONTINUED_GOODS_STATUS = "DISCONTINUANCE";

    private final GoodsRepository goodsRepository;
    private final GoodsValidator goodsValidator;

    public Page<GoodsInfo> findAll(final Pageable pageable) {
        List<GoodsInfo> goodsList = goodsRepository.findAll()
                .stream()
                .map(goods -> GoodsInfo.of(goods, goods.getDiscount()))
                .toList();
        return new PageImpl<>(goodsList, pageable, goodsList.size());
    }


    @Transactional
    public void deleteById(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
        goodsValidator.validateGoodsSalesStatus(goods, ON_SALE);
        goodsRepository.delete(goods);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Goods fetchGoodsEntity(Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
    }

}

package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.DiscontinuedGoodsException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.OnSaleGoodsException;
import com.cherrydev.cherrymarketbe.server.application.common.service.file.FileService;
import com.cherrydev.cherrymarketbe.server.application.common.utils.PagingUtil;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.MyPage;
import com.cherrydev.cherrymarketbe.server.domain.discount.dto.request.UpdateDiscountCondition;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.*;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    public static final String DISCONTINUED_GOODS_STATUS = "DISCONTINUANCE";

    private final GoodsMapper goodsMapper;
    private final GoodsValidator goodsValidator;
    private final FileService fileService;

    @Transactional
    public void save(RequestAddGoods requestAddGoods, List<MultipartFile> images) {
        fileService.uploadMultipleFiles(images, "goods");

        goodsMapper.updateStatusWhenNewGoods(requestAddGoods.getGoodsCode(), DISCONTINUED_GOODS_STATUS);
        goodsMapper.save(requestAddGoods);
    }

    public MyPage<GoodsInfo> findAll(final Pageable pageable) {
        return PagingUtil.createPage(pageable, convertToGoodsInfoList(goodsMapper.findAll()));
    }

    public MyPage<GoodsInfo> findAllByConditions(final Pageable pageable, SearchCondition condition) {
        List<Goods> goodsList = getGoodsEntityByContidion(condition);
        return PagingUtil.createPage(pageable, convertToGoodsInfoList(goodsList));
    }

    public MyPage<GoodsDetailInfo> findAllDetailedByConditions(final Pageable pageable, SearchCondition condition) {
        List<Goods> goodsList = getGoodsEntityByContidion(condition);
        return PagingUtil.createPage(pageable, convertToGoodsDetailInfoList(goodsList));
    }

    public ToCartResponse findToCart(Long goodsId) {

        ToCartResponse toCartResponse = goodsMapper.findToCart(goodsId);

        return ToCartResponse.builder()
                .goodsId(toCartResponse.getGoodsId())
                .goodsName(toCartResponse.getGoodsName())
                .goodsCode(toCartResponse.getGoodsCode())
                .price(toCartResponse.getPrice())
                .inventory(toCartResponse.getInventory())
                .storageType(toCartResponse.getStorageType())
                .salesStatus(toCartResponse.getSalesStatus())
                .discountRate(toCartResponse.getDiscountRate())
                .discountedPrice(toCartResponse.getDiscountRate())
                .build();
    }


    public MyPage<GoodsInfo> getDiscountedGoods(Pageable pageable) {
        List<Goods> goodsList = goodsMapper.findDiscountedGoods();
        return PagingUtil.createPage(pageable, convertToGoodsInfoList(goodsList));
    }

    @Transactional
    public Integer updateDiscountByConditions(Long discountId, UpdateDiscountCondition condition) {
        return goodsMapper.updateDiscount(discountId, condition);
    }

    @Transactional
    public void updateGoodsInventory(Long goodsId, int quantity) {
        goodsMapper.updateGoodsInventory(goodsId, quantity);
    }

    @Transactional
    public void deleteById(Long goodsId) {
        Goods goods = goodsMapper.findById(goodsId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_GOODS));
        goodsValidator.checkProductIsNotInSale(goods);
        goodsMapper.delete(goods);
    }

    private List<Goods> getGoodsEntityByContidion(SearchCondition condition) {
        Map<String, Object> params = new HashMap<>();
        params.put("goodsName", condition.goodsName());
        params.put("categoryId", condition.categoryId());
        params.put("makerId", condition.makerId());
        params.put("goodsCode", condition.goodsCode());

        return goodsMapper.searchGoods(params);
    }

    private List<GoodsInfo> convertToGoodsInfoList(List<Goods> goodsList) {
        return goodsList.stream()
                .map(GoodsInfo::of)
                .toList();
    }

    private List<GoodsDetailInfo> convertToGoodsDetailInfoList(List<Goods> goodsList) {
        return goodsList.stream()
                .map(GoodsDetailInfo::of)
                .toList();
    }

}

package com.cherrydev.cherrymarketbe.goods.service;

import com.cherrydev.cherrymarketbe.goods.dto.*;
import com.cherrydev.cherrymarketbe.goods.repository.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsMapper goodsMapper;

    @Transactional
    public void save(GoodsRegistrationDto goodsRegistrationDto) {
        // 새 상품이 입력 되었을 떄 동일한 Code를 갖고 있다면 기존 상품의 판매 상태를 변경
        goodsMapper.updateStatusWhenNewGoods(goodsRegistrationDto.getGoodsCode(), "DISCONTINUANCE");
        goodsMapper.save(goodsRegistrationDto);
    }

    @Transactional
    public List<GoodsRegistrationDto> findAll() {

        return goodsMapper.findAll();
    }

    @Transactional
    public DiscountCalcDto findBasicInfo(Long goodsId) {
        GoodsBasicInfoDto basicInfo = goodsMapper.findBasicInfo(goodsId);
        // 할인된 금액 계산
        Integer discountedPrice = discountedPrice = (basicInfo.getDiscountRate() != null) ? basicInfo.getPrice() - (basicInfo.getPrice() * basicInfo.getDiscountRate() / 100) : null;

        // 할인된 금액과 함께 Dto 반환
        return DiscountCalcDto.builder()
                       .goodsId(basicInfo.getGoodsId())
                       .goodsName(basicInfo.getGoodsName())
                       .description(basicInfo.getDescription())
                       .price(basicInfo.getPrice())
                       .discountRate(basicInfo.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .build();
    }

    @Transactional
    public List<DiscountCalcDto> findByCategoryId(Long categoryId) {
        List<GoodsBasicInfoDto> basicInfoList = goodsMapper.findByCategoryId(categoryId);

        return basicInfoList.stream()
                       .map(basicInfo -> {
                           Integer discountedPrice = discountedPrice = (basicInfo.getDiscountRate() != null) ? basicInfo.getPrice() - (basicInfo.getPrice() * basicInfo.getDiscountRate() / 100) : null;
                           return DiscountCalcDto.builder()
                                          .goodsId(basicInfo.getGoodsId())
                                          .goodsName(basicInfo.getGoodsName())
                                          .description(basicInfo.getDescription())
                                          .price(basicInfo.getPrice())
                                          .discountRate(basicInfo.getDiscountRate())
                                          .discountedPrice(discountedPrice)
                                          .build();
                       })
                       .collect(Collectors.toList());
    }

    @Transactional
    public ToCartResponseDto findToCart(Long goodsId) {

        ToCartDto toCartDto = goodsMapper.findToCart(goodsId);
        Integer discountedPrice = (toCartDto.getDiscountRate() != null) ? toCartDto.getPrice() - (toCartDto.getPrice() * toCartDto.getDiscountRate() / 100) : null;

        return ToCartResponseDto.builder()
                       .goodsId(toCartDto.getGoodsId())
                       .goodsName(toCartDto.getGoodsName())
                       .price(toCartDto.getPrice())
                       .inventory(toCartDto.getInventory())
                       .storageType(toCartDto.getStorageType())
                       .salesStatus(toCartDto.getSalesStatus())
                       .discountRate(toCartDto.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .build();
    }

    @Transactional
    public GoodsDetailResponseDto findDetailById(Long goodsId) {
        GoodsDetailDto goodsDetailDto = goodsMapper.findDetailById(goodsId);
        Integer discountedPrice = (goodsDetailDto.getDiscountRate() != null) ? goodsDetailDto.getPrice() - (goodsDetailDto.getPrice() * goodsDetailDto.getDiscountRate() / 100) : null;

        return GoodsDetailResponseDto.builder()
                       .goodsId(goodsDetailDto.getGoodsId())
                       .goodsCode(goodsDetailDto.getGoodsCode())
                       .goodsName(goodsDetailDto.getGoodsName())
                       .description(goodsDetailDto.getDescription())
                       .price(goodsDetailDto.getPrice())
                       .inventory(goodsDetailDto.getInventory())
                       .storageType(goodsDetailDto.getStorageType())
                       .capacity(goodsDetailDto.getCapacity())
                       .expDate(goodsDetailDto.getExpDate())
                       .allergyInfo(goodsDetailDto.getAllergyInfo())
                       .originPlace(goodsDetailDto.getOriginPlace())
                       .salesStatus(goodsDetailDto.getSalesStatus())
                       .discountRate(goodsDetailDto.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .makerName(goodsDetailDto.getMakerName())
                       .build();
    }

    @Transactional
    public GoodsDetailResponseDto findDetailByCode(String goodsCode) {
        GoodsDetailDto goodsDetailDto = goodsMapper.findDetailByCode(goodsCode);
        Integer discountedPrice = (goodsDetailDto.getDiscountRate() != null) ? goodsDetailDto.getPrice() - (goodsDetailDto.getPrice() * goodsDetailDto.getDiscountRate() / 100) : null;

        return GoodsDetailResponseDto.builder()
                       .goodsId(goodsDetailDto.getGoodsId())
                       .goodsCode(goodsDetailDto.getGoodsCode())
                       .goodsName(goodsDetailDto.getGoodsName())
                       .description(goodsDetailDto.getDescription())
                       .price(goodsDetailDto.getPrice())
                       .inventory(goodsDetailDto.getInventory())
                       .storageType(goodsDetailDto.getStorageType())
                       .capacity(goodsDetailDto.getCapacity())
                       .expDate(goodsDetailDto.getExpDate())
                       .allergyInfo(goodsDetailDto.getAllergyInfo())
                       .originPlace(goodsDetailDto.getOriginPlace())
                       .salesStatus(goodsDetailDto.getSalesStatus())
                       .discountRate(goodsDetailDto.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .makerName(goodsDetailDto.getMakerName())
                       .build();
    }

    @Transactional
    public void deleteById(Long goodId) {
        goodsMapper.deleteById(goodId);
    }

}

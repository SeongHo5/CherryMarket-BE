package com.cherrydev.cherrymarketbe.goods.service;

import com.cherrydev.cherrymarketbe.goods.dto.*;
import com.cherrydev.cherrymarketbe.goods.exception.DiscontinuedGoodsException;
import com.cherrydev.cherrymarketbe.goods.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.goods.exception.OnSaleGoodsException;
import com.cherrydev.cherrymarketbe.goods.repository.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cherrydev.cherrymarketbe.goods.exception.enums.GoodsExceptionStatus.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsMapper goodsMapper;

    /* Insert */
    @Transactional
    public void save(GoodsDto goodsDto) {
        // 새 상품이 입력 되었을 떄 동일한 Code를 갖고 있다면 기존 상품의 판매 상태를 변경
        goodsMapper.updateStatusWhenNewGoods(goodsDto.getGoodsCode(), "DISCONTINUANCE");
        goodsMapper.save(goodsDto);
    }

    /* Select */
    @Transactional
    public List<GoodsDto> findAll(String sortBy) {
        return goodsMapper.findAll(sortBy);
    }

    @Transactional
    public DiscountCalcDto findBasicInfo(Long goodsId) {
        GoodsBasicInfoDto basicInfo = goodsMapper.findBasicInfo(goodsId);

        if(basicInfo == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

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
    public List<DiscountCalcDto> findByCategoryId(Long categoryId, String sortBy) {
        List<GoodsBasicInfoDto> basicInfoList = goodsMapper.findByCategoryId(categoryId, sortBy);

        if(basicInfoList.isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

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

        if(goodsDetailDto == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

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

        if(goodsDetailDto == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

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
    public List<DiscountCalcDto> findByName(String goodsName, String sortBy) {
        List<GoodsBasicInfoDto> basicInfoList = goodsMapper.findByName(goodsName, sortBy);
        List<DiscountCalcDto> discountCalcDtoList = new ArrayList<>();

        if(basicInfoList.isEmpty()){
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        for(GoodsBasicInfoDto basicInfoDto : basicInfoList) {
            // 할인된 금액 계산
            Integer discountedPrice = discountedPrice = (basicInfoDto.getDiscountRate() != null) ? basicInfoDto.getPrice() - (basicInfoDto.getPrice() * basicInfoDto.getDiscountRate() / 100) : null;

            DiscountCalcDto discountCalcDto = DiscountCalcDto.builder()
                                                      .goodsId(basicInfoDto.getGoodsId())
                                                      .goodsName(basicInfoDto.getGoodsName())
                                                      .description(basicInfoDto.getDescription())
                                                      .price(basicInfoDto.getPrice())
                                                      .discountRate(basicInfoDto.getDiscountRate())
                                                      .discountedPrice(discountedPrice)
                                                      .build();
            discountCalcDtoList.add(discountCalcDto);
        }
        return discountCalcDtoList;
    }

    /* Update */
    @Transactional
    public List<GoodsDto> updateDiscountByMaker(Long discountId, Long makerId) {
        goodsMapper.updateDiscountByMaker(discountId, makerId);

        List<GoodsDto> updatedGoodsList = goodsMapper.findByMakerForDiscount(makerId);
        if(updatedGoodsList.isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        return updatedGoodsList;
    }

    @Transactional
    public List<GoodsDto> updateDiscountByCategory(Long discountId, Long categoryId) {
        goodsMapper.updateDiscountByCategory(discountId, categoryId);

        List<GoodsDto> updatedGoodsList = goodsMapper.findByCategoryForDiscount(categoryId);
        if(updatedGoodsList.isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        return updatedGoodsList;
    }

    @Transactional
    public GoodsDto updateDiscountByGoodsId(Long discountId, Long goodsId) {
        GoodsDto goodsInfo = goodsMapper.findByGoodsIdForDiscount(goodsId);

        // 상품이 존재하지 않을 경우
        if(goodsInfo == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }
        // 단종된 상품일 경우
        if("DISCONTINUANCE" .equals(goodsInfo.getSalesStatus())) {
            throw new DiscontinuedGoodsException(DISCONTINUED_GOODS);
        }
        goodsMapper.updateDiscountByGoodsId(discountId, goodsId);
        return goodsInfo;
    }

    /* Delete */
    @Transactional
    public void deleteById(Long goodsId) {

        GoodsDto goodsInfo = goodsMapper.findById(goodsId);

        // 상품이 존재하지 않을 경우
        if(goodsInfo == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        // 판매중인 상품일 경우
        if("ON_SALE" .equals(goodsInfo.getSalesStatus())) {
            throw new OnSaleGoodsException(ON_SALE_GOODS);
        }

        goodsMapper.deleteById(goodsId);
    }

}

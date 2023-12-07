package com.cherrydev.cherrymarketbe.goods.service;

import com.cherrydev.cherrymarketbe.goods.dto.DiscountCalcDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsBasicInfoDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsRegistrationDto;
import com.cherrydev.cherrymarketbe.goods.repository.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsMapper goodsMapper;

    @Transactional
    public void save(GoodsRegistrationDto goodsRegistrationDto) {
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
                       .price(basicInfo.getPrice())
                       .discountRate(basicInfo.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .build();
    }

    @Transactional
    public void deleteById(Long goodId){
        goodsMapper.deleteById(goodId);
    }

}

package com.cherrydev.cherrymarketbe.discount.repository;

import com.cherrydev.cherrymarketbe.discount.dto.DiscountDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscountMapper {

    List<DiscountDto> findAll();

    DiscountDto findById(Long discountId);

    DiscountDto findByCode(String discountCode);

    void save(DiscountDto discountDto);

    void updateById(DiscountDto discountDto);

    void deleteById(Long discountId);
}

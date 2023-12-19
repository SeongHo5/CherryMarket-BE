package com.cherrydev.cherrymarketbe.discount.service;

import com.cherrydev.cherrymarketbe.discount.dto.DiscountDto;
import com.cherrydev.cherrymarketbe.discount.repository.DiscountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountMapper discountMapper;

    @Transactional
    public List<DiscountDto> findAll() {
        return discountMapper.findAll();
    }

    @Transactional
    public DiscountDto findById(Long discountId){
        return discountMapper.findById(discountId);
    }

    @Transactional
    public List<DiscountDto> findByCode(String discountCode){
        return discountMapper.findByCode(discountCode);
    }

    @Transactional
    public void save(DiscountDto discountDto) {
        discountMapper.save(discountDto);
    }

    @Transactional
    public void updateById(DiscountDto discountDto) {
        discountMapper.updateById(discountDto);
    }

    @Transactional
    public void deleteById(Long discountId){
        discountMapper.deleteById(discountId);
    }
}

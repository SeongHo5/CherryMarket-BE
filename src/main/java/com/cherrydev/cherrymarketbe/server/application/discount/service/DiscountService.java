package com.cherrydev.cherrymarketbe.server.application.discount.service;

import com.cherrydev.cherrymarketbe.server.domain.discount.dto.request.RequestDiscount;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.payment.DiscountMapper;
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

    public List<RequestDiscount> findAll() {
        return discountMapper.findAll();
    }

    public RequestDiscount findById(Long discountId){
        return discountMapper.findById(discountId);
    }

    public List<RequestDiscount> findByCode(String discountCode){
        return discountMapper.findByCode(discountCode);
    }

    @Transactional
    public void save(RequestDiscount requestDiscount) {
        discountMapper.save(requestDiscount);
    }

    @Transactional
    public void updateById(RequestDiscount requestDiscount) {
        discountMapper.updateById(requestDiscount);
    }

    @Transactional
    public void deleteById(Long discountId){
        discountMapper.deleteById(discountId);
    }
}

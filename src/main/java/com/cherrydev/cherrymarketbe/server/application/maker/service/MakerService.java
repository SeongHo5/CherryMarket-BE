package com.cherrydev.cherrymarketbe.server.application.maker.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.FormatException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.maker.dto.response.MakerInfo;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.MakerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INVALID_BUSINESS_NUMBER_FORMAT;
import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_MAKER;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MakerService {

    private final MakerMapper makerMapper;

    @Transactional
    public List<MakerInfo> findAll(String sortBy) {
        return makerMapper.findAll(sortBy);
    }

    public MakerInfo findById(Long makerId) {
        MakerInfo makerInfo = makerMapper.findById(makerId);
        if(makerInfo == null) {
            throw new NotFoundException(NOT_FOUND_MAKER);
        }
        return makerInfo;
    }

    public List<MakerInfo> findByName(String makerName){
        List<MakerInfo> makerInfo = makerMapper.findByName(makerName);
        if(makerInfo.isEmpty()){
            throw new NotFoundException(NOT_FOUND_MAKER);
        }
        return makerInfo;
    }

    @Transactional
    public MakerInfo findByBusinessNumber(String businessNumber) {

        if (!businessNumber.matches("^\\d{3}-\\d{2}-\\d{5}$")) {
            throw new FormatException(INVALID_BUSINESS_NUMBER_FORMAT);
        }

        MakerInfo makerInfo = makerMapper.findByBusinessNumber(businessNumber);
        if(makerInfo == null) {
            throw new NotFoundException(NOT_FOUND_MAKER);
        }
        return makerInfo;
    }

    @Transactional
    public void save(MakerInfo makerInfo) {
        makerMapper.save(makerInfo);
    }

    @Transactional
    public void deleteById(Long makerId) {
        MakerInfo makerInfo = makerMapper.findById(makerId);
        if(makerInfo == null) {
            throw new NotFoundException(NOT_FOUND_MAKER);
        }
        makerMapper.deleteById(makerId);
    }

    @Transactional
    public void updateById(MakerInfo makerInfo) {
        MakerInfo checkDto = makerMapper.findById(makerInfo.getMakerId());
        if(checkDto == null) {
            throw new NotFoundException(NOT_FOUND_MAKER);
        }
        makerMapper.updateById(makerInfo);
    }
}

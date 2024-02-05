package com.cherrydev.cherrymarketbe.maker.service;

import com.cherrydev.cherrymarketbe.goods.exception.FormatException;
import com.cherrydev.cherrymarketbe.goods.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.maker.dto.MakerDto;
import com.cherrydev.cherrymarketbe.maker.repository.MakerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.goods.exception.enums.GoodsExceptionStatus.*;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MakerService {

    private final MakerMapper makerMapper;

    @Transactional
    public List<MakerDto> findAll(String sortBy) {
        return makerMapper.findAll(sortBy);
    }

    public MakerDto findById(Long makerId) {
        MakerDto makerDto = makerMapper.findById(makerId);
        if(makerDto == null) {
            throw new NotFoundException(MAKER_NOT_FOUND);
        }
        return makerDto;
    }

    public List<MakerDto> findByName(String makerName){
        List<MakerDto> makerDto = makerMapper.findByName(makerName);
        if(makerDto.isEmpty()){
            throw new NotFoundException(MAKER_NOT_FOUND);
        }
        return makerDto;
    }

    @Transactional
    public MakerDto findByBusinessNumber(String businessNumber) {

        if (!businessNumber.matches("^\\d{3}-\\d{2}-\\d{5}$")) {
            throw new FormatException(INVALID_BUSINESS_NUMBER_FORMAT);
        }

        MakerDto makerDto = makerMapper.findByBusinessNumber(businessNumber);
        if(makerDto == null) {
            throw new NotFoundException(MAKER_NOT_FOUND);
        }
        return makerDto;
    }

    @Transactional
    public void save(MakerDto makerDto) {
        makerMapper.save(makerDto);
    }

    @Transactional
    public void deleteById(Long makerId) {
        MakerDto makerDto = makerMapper.findById(makerId);
        if(makerDto == null) {
            throw new NotFoundException(MAKER_NOT_FOUND);
        }
        makerMapper.deleteById(makerId);
    }

    @Transactional
    public void updateById(MakerDto makerDto) {
        MakerDto checkDto = makerMapper.findById(makerDto.getMakerId());
        if(checkDto == null) {
            throw new NotFoundException(MAKER_NOT_FOUND);
        }
        makerMapper.updateById(makerDto);
    }
}

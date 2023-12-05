package com.cherrydev.cherrymarketbe.maker.service;

import com.cherrydev.cherrymarketbe.maker.dto.MakerDto;
import com.cherrydev.cherrymarketbe.maker.repository.MakerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MakerService {

    private final MakerMapper makerMapper;

    @Transactional
    public List<MakerDto> findAll() {
        List<MakerDto> makerDtoList = makerMapper.findAll();
        return makerDtoList;
    }

    @Transactional
    public MakerDto findByBusinessNumber(String businessNumber) {
        return makerMapper.findByBusinessNumber(businessNumber);
    }

    @Transactional
    public void save(MakerDto makerDto){
        makerMapper.save(makerDto);
    }

    @Transactional
    public void deleteById(Long makerId){
        makerMapper.deleteById(makerId);
    }

    @Transactional
    public void updateById(MakerDto makerDto){
        makerMapper.updateById(makerDto);
    }
}

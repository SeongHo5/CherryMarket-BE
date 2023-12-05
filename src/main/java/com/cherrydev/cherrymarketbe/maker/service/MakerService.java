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
    public List<MakerDto> findAllMaker() {
        List<MakerDto> makerDtoList = makerMapper.findAllMaker();
        return makerDtoList;
    }

    @Transactional
    public MakerDto findMakerByBusinessNumber(String businessNumber) {
        return makerMapper.findMakerByBusinessNumber(businessNumber);
    }

    @Transactional
    public void saveMaker(MakerDto makerDto){
        makerMapper.saveMaker(makerDto);
    }

    @Transactional
    public void deleteMakerById(Long makerId){
        makerMapper.deleteMakerById(makerId);
    }

    @Transactional
    public void updateMakerById(MakerDto makerDto){
        makerMapper.updateMakerById(makerDto);
    }
}

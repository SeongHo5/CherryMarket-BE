package com.cherrydev.cherrymarketbe.maker.repository;

import com.cherrydev.cherrymarketbe.maker.dto.MakerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MakerMapper {

    List<MakerDto> findAllMaker();

    MakerDto findMakerByBusinessNumber(String businessNumber);

    void saveMaker(MakerDto makerDto);

    void deleteMakerById(Long makerId);

    void updateMakerById(MakerDto makerDto);
}

package com.cherrydev.cherrymarketbe.maker.repository;

import com.cherrydev.cherrymarketbe.maker.dto.MakerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MakerMapper {

    List<MakerDto> findAll();

    MakerDto findByBusinessNumber(String businessNumber);

    void save(MakerDto makerDto);

    void deleteById(Long makerId);

    void updateById(MakerDto makerDto);
}

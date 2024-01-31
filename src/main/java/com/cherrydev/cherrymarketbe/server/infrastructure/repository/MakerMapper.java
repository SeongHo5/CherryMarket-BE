package com.cherrydev.cherrymarketbe.server.infrastructure.repository;

import com.cherrydev.cherrymarketbe.server.domain.maker.dto.response.MakerInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MakerMapper {

    List<MakerInfo> findAll(String sortBy);

    MakerInfo findById(Long makerId);

    List<MakerInfo> findByName(String makerName);

    MakerInfo findByBusinessNumber(String businessNumber);

    void save(MakerInfo makerInfo);

    void deleteById(Long makerId);

    void updateById(MakerInfo makerInfo);
}

package com.cherrydev.cherrymarketbe.account.repository;

import com.cherrydev.cherrymarketbe.account.entity.Agreement;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AgreementMapper {

    void save(Agreement agreement);

    Optional<Agreement> findByAccountId(Long accountId);

    void update(Agreement agreement);
}

package com.cherrydev.cherrymarketbe.account.repository;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.enums.RegisterType;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AccountMapper {

    void save(Account account);

    void delete(Account account);

    Optional<Account> findByEmail(String email);

    boolean existByEmailAndRegistType(String email, RegisterType registType);

    boolean existByEmail(String email);



}

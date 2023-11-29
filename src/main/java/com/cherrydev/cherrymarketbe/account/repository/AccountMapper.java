package com.cherrydev.cherrymarketbe.account.repository;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.admin.dto.AdminUserInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {
    void save(Account account);

    void delete(Account account);

    void updateAccountInfo(Account account);

    void updateAccountRole(Account account);

    void updateAccountStatus(Account account);

    void updateAccountPassword(Account account);

    Optional<Account> findByaccountId(Long id);

    Optional<Account> findByEmail(String email);

    List<AdminUserInfoDto> findAll();

    boolean existByEmailAndRegistType(String email, RegisterType registType);

    boolean existByEmail(String email);

}

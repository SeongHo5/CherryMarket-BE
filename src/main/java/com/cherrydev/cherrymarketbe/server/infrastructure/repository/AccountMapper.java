package com.cherrydev.cherrymarketbe.server.infrastructure.repository;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.AdminUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {

    // ==================== INSERT ==================== //

    void save(Account account);

    // ==================== DELETE ==================== //

    void delete(Account account);

    // ==================== UPDATE ==================== //

    void updateAccountInfo(Account account);

    void updateAccountRole(Account account);

    void updateAccountStatus(Account account);

    // ==================== SELECT ==================== //

    List<AdminUserInfo> findAll();

    Optional<Account> findByaccountId(Long id);

    Optional<Account> findByEmail(String email);

    RegisterType getRegisterTypeByEmail(String email);

    boolean existByEmailAndRegisterType(String email, RegisterType registType);

    boolean existByEmail(String email);

    // ==================== PROCEDURE ==================== //

    void deleteInactiveAccount();

    void releaseRestrictedAccount();

}

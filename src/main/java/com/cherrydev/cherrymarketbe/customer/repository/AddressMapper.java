package com.cherrydev.cherrymarketbe.customer.repository;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddressInfoDto;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {

    // ==================== INSERT ==================== //

    void save(CustomerAddress customerAddress);

    // ==================== DELETE ==================== //

    void delete(CustomerAddress customerAddress);

    void deleteAllByAccountId(Account account);

    // ==================== UPDATE ==================== //

    void update(CustomerAddress customerAddress);

    // ==================== SELECT ==================== //

    List<AddressInfoDto> findAllByAccountId(Account account);

    int countAllByAccountId(Account account);

    boolean existByAccountIdAndIsDefault(CustomerAddress customerAddress);


}

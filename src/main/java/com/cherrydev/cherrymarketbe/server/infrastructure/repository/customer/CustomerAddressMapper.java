package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.response.AddressInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CustomerAddressMapper {

    // ==================== INSERT ==================== //

    void save(CustomerAddress customerAddress);

    // ==================== DELETE ==================== //

    void delete(CustomerAddress customerAddress);

    void deleteAllByAccountId(Account account);


    // ==================== UPDATE ==================== //

    void update(CustomerAddress customerAddress);

    // ==================== SELECT ==================== //

    List<AddressInfo> findAllByAccountId(Account account);

    Optional<CustomerAddress> findByIdAndAccountId(Long accountId, Long addressId);

    int countAllByAccountId(Account account);

    boolean existByAccountIdAndIsDefault(CustomerAddress customerAddress);


    CustomerAddress findDefaultByAccountId(CustomerAddress customerAddress);

    void updateDefaultAddress(CustomerAddress customerAddress);
}

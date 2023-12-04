package com.cherrydev.cherrymarketbe.customer.repository;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerReward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RewardMapper {

    void save(final CustomerReward customerReward);


    List<CustomerReward> findAllByAccount(Account account);
}

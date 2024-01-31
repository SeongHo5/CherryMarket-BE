package com.cherrydev.cherrymarketbe.server.application.admin.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.MyPage;
import com.cherrydev.cherrymarketbe.server.application.common.utils.PagingUtil;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserRole;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserStatus;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.AdminUserInfo;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType.LOCAL;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public MyPage<AdminUserInfo> getAllAcounts(
            final Pageable pageable
    ) {
        return PagingUtil.createPage(pageable, accountMapper::findAll);
    }

    @Transactional
    public void modifyAccountRole(final ModifyUserRole roleRequestDto) {
        Account account = accountMapper.findByEmail(roleRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));

        checkAccountRegisterType(account);

        account.updateAccountRole(roleRequestDto.getNewRole());
        accountMapper.updateAccountRole(account);
    }

    @Transactional
    public void modifyAccountStatus(final ModifyUserStatus statusRequestDto) {
        Account account = accountMapper.findByEmail(statusRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
        LocalDate restrictionEndDate = LocalDate.parse(statusRequestDto.getRestrictedUntil());
        checkAccountRestrictionEndDate(restrictionEndDate);

        account.updateAccountStatus(statusRequestDto.getNewStatus());
        account.updateRestrictedUntil(restrictionEndDate);

        accountMapper.updateAccountStatus(account);
    }


    // =============== PRIVATE METHODS =============== //

    private void checkAccountRegisterType(final Account account) {
        if (!account.getRegistType().equals(LOCAL)) {
            throw new ServiceFailedException(CHANGE_ROLE_FORBIDDEN);
        }
    }

    private void checkAccountRestrictionEndDate(final LocalDate restrictionEndDate) {
        if (restrictionEndDate == null) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }
        if (restrictionEndDate.isBefore(LocalDate.now())) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }
    }

}

package com.cherrydev.cherrymarketbe.admin.service;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.admin.dto.*;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.LOCAL;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<AdminUserInfoDto>> getAllAcounts(
            final Pageable pageable
    ) {
        MyPage<AdminUserInfoDto> infoPage = createPage(pageable, accountMapper::findAll);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Transactional
    public void modifyAccountRole(final ModifyUserRoleDto roleRequestDto) {
        Account account = accountMapper.findByEmail(roleRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));

        checkAccountRegisterType(account);

        account.updateAccountRole(roleRequestDto.getNewRole());
        accountMapper.updateAccountRole(account);
    }

    @Transactional
    public void modifyAccountStatus(final ModifyUserStatusDto statusRequestDto) {
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

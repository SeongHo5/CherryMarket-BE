package com.cherrydev.cherrymarketbe.admin.service;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.admin.dto.AdminUserInfoDto;
import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserRoleRequestDto;
import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserStatusByAdminDto;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.LOCAL;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.CHANGE_ROLE_FORBIDDEN;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_FOUND_ACCOUNT;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    @Value("${oauth.kakao.adminKey}")
    private String kakaoAdminKey;

    private final AccountMapper accountMapper;
    private final RestTemplate restTemplate;

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
    public void modifyAccountRole(final ModifyUserRoleRequestDto roleRequestDto) {
        Account account = getAccountByEmail(roleRequestDto.getEmail());

        checkAccountRegisterType(account);

        account.updateAccountRole(roleRequestDto.getNewRole());
        accountMapper.updateAccountRole(account);
    }

    @Transactional
    public void modifyAccountStatus(final ModifyUserStatusByAdminDto statusRequestDto) {
        Account account = getAccountByEmail(statusRequestDto.getEmail());

        account.updateAccountStatus(statusRequestDto.getNewStatus());
        account.updateRestrictedUntil(statusRequestDto.getRestrictedUntil());

        accountMapper.updateAccountStatus(account);
    }

    // 공지사항 작성 //

    // =============== PRIVATE METHODS =============== //

    private Account getAccountByEmail(String email) {
        return accountMapper.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
    }

    private void checkAccountRegisterType(final Account account) {
        if (!account.getRegistType().equals(LOCAL)) {
            throw new ServiceFailedException(CHANGE_ROLE_FORBIDDEN);
        }
    }
}

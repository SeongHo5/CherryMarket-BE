package com.cherrydev.cherrymarketbe.server.application.account.service;

import com.cherrydev.cherrymarketbe.server.application.account.event.AccountRegistrationEvent;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.common.utils.CodeGenerator;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestModifyAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Agreement;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AccountMapper;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AgreementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType.LOCAL;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AgreementMapper agreementMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final AccountValidator accountValidator;
    @Transactional
    public void createAccount(final RequestSignUp requestSignUp) {
        String requestedEmail = requestSignUp.getEmail();
        String requestedUsername = requestSignUp.getName();

        accountValidator.checkUsernameIsProhibited(requestedUsername);
        accountValidator.checkEmailIsDuplicated(requestedEmail);

        String encodedPassword = passwordEncoder.encode(requestSignUp.getPassword());

        Account account = Account.of(requestSignUp, encodedPassword, LOCAL);
        accountMapper.save(account);

        Agreement agreement = Agreement.of(requestSignUp);
        agreementMapper.save(agreement);

        publishWelcomeEvent(account);
    }

    @Transactional
    public void createAccountByOAuth(final OAuthAccountInfo oAuthAccountInfo, final String provider) {
        String email = oAuthAccountInfo.getEmail();
        String name = oAuthAccountInfo.getName();
        String encodedPassword = passwordEncoder.encode(CodeGenerator.generateRandomCode(10));

        accountValidator.checkUsernameIsProhibited(name);
        accountValidator.checkEmailIsDuplicated(email);

        Account account = Account.of(oAuthAccountInfo, encodedPassword, RegisterType.valueOf(provider));

        accountMapper.save(account);
        publishWelcomeEvent(account);
    }

    @Transactional(readOnly = true)
    public AccountInfo getAccountInfo(final AccountDetails accountDetails) {
        Account account = accountDetails.getAccount();
        return AccountInfo.of(account);
    }

    @Transactional
    public AccountInfo modifyAccount(
            final AccountDetails accountDetails,
            final RequestModifyAccountInfo requestDto
    ) {
        Account account = accountDetails.getAccount();

        if (requestDto.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
            account.updatePassword(encodedPassword);
        }

        if (requestDto.getContact() != null) {
            account.updateContact(requestDto.getContact());
        }

        if (requestDto.getBirthdate() != null) {
            LocalDate birthdate = LocalDate.parse(requestDto.getBirthdate());
            account.updateBirthdate(birthdate);
        }

        accountMapper.updateAccountInfo(account);

        return AccountInfo.of(account);
    }

    @Transactional
    public void modifyAccount(final Account account) {
        accountMapper.updateAccountInfo(account);
    }

    @Transactional
    public void deleteAccount(final AccountDetails accountDetails) {
        accountMapper.delete(accountDetails.getAccount());
    }

    /**
     * 이메일로 사용자를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 조회된 사용자
     */
    @Transactional(readOnly = true)
    public Account findAccountByEmail(final String email) {
        return accountMapper.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
    }

    @Transactional(readOnly = true)
    public boolean existByEmail(final String email) {
        return accountMapper.existByEmail(email);
    }

    @Transactional(readOnly = true)
    public RegisterType getRegisterTypeByEmail(final String email) {
        return accountMapper.getRegisterTypeByEmail(email);
    }

    // =============== PRIVATE METHODS =============== //

    private void publishWelcomeEvent(Account account) {
        AccountRegistrationEvent event = new AccountRegistrationEvent(this, account);
        eventPublisher.publishEvent(event);
    }

}

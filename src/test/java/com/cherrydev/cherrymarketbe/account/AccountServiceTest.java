package com.cherrydev.cherrymarketbe.account;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.dto.AccountInfoDto;
import com.cherrydev.cherrymarketbe.account.dto.ModifyAccountInfoRequestDto;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.account.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.auth.service.impl.AuthServiceImpl;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.factory.AccountFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    @InjectMocks
    AccountServiceImpl accountService;

    @Autowired
    @InjectMocks
    AuthServiceImpl authService;
    @Mock
    AccountMapper accountMapper;


    @Test
    void 회원가입_이메일_중복() {
        // Given
        SignUpRequestDto signUpRequestDto = AccountFactory.createSignUpRequestDtoA();

        // When
        when(accountMapper.existByEmail(signUpRequestDto.getEmail())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> accountService.createAccount(signUpRequestDto))
                .isInstanceOf(DuplicatedException.class)
                .satisfies(e -> assertThat(DuplicatedException.class.cast(e).getStatusCode())
                        .isEqualTo(CONFLICT_ACCOUNT.getStatusCode()))
                .hasMessageContaining(CONFLICT_ACCOUNT.getMessage());
    }

    @Test
    void 회원가입_금지된_이름() {
        // Given
        SignUpRequestDto signUpRequestDto = AccountFactory.createSignUpRequestDtoB();

        // When
        assertThatThrownBy(() -> accountService.createAccount(signUpRequestDto))
                .isInstanceOf(AuthException.class)
                .satisfies(e -> assertThat(AuthException.class.cast(e).getStatusCode())
                        .isEqualTo(PROHIBITED_USERNAME.getStatusCode()))
                .hasMessageContaining(PROHIBITED_USERNAME.getMessage());
    }

    @Test
    void 로그인_실패_없는_사용자() {
        // Given
        SignInRequestDto signInRequestDto = new SignInRequestDto(
                "test1@noting.com",
                "Testuser12#"
        );

        // When
        when(accountMapper.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> authService.signIn(signInRequestDto))
                .isInstanceOf(NotFoundException.class)
                .satisfies(e -> assertThat(NotFoundException.class.cast(e).getStatusCode())
                        .isEqualTo(NOT_FOUND_ACCOUNT.getStatusCode()))
                .hasMessageContaining(NOT_FOUND_ACCOUNT.getMessage());
    }

    @Test
    void 로그인_실패_이용제한된_사용자() {
        // Given
        SignInRequestDto signInRequestDto = new SignInRequestDto(
                "test1@marketcherry.com",
                "Testuser12#"
        );
        Account account = AccountFactory.createAccountA();

        // When
        when(accountMapper.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(account));

        // Then
        assertThatThrownBy(() -> authService.signIn(signInRequestDto))
                .isInstanceOf(AuthException.class)
                .satisfies(e -> assertThat(((AuthException) e).getStatusCode())
                        .isEqualTo(RESTRICTED_ACCOUNT.getStatusCode()))
                .hasMessageContaining(RESTRICTED_ACCOUNT.getMessage());
    }

    @Test
    void 로그인_실패_탈퇴한_사용자() {
        // Given
        SignInRequestDto signInRequestDto = new SignInRequestDto(
                "test2@marketcherry.com",
                "Testuser12#"
        );
        Account account = AccountFactory.createAccountB();

        // When
        when(accountMapper.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(account));

        // Then
        assertThatThrownBy(() -> authService.signIn(signInRequestDto))
                .isInstanceOf(NotFoundException.class)
                .satisfies(e -> assertThat(((NotFoundException) e).getStatusCode())
                        .isEqualTo(DELETED_ACCOUNT.getStatusCode()))
                .hasMessageContaining(DELETED_ACCOUNT.getMessage());
    }

    @Test
    void 내_정보_보기() {
        // Given
        Account account = AccountFactory.createAccountA();

        // When
        when(accountMapper.findByaccountId(account.getAccountId())).thenReturn(Optional.of(account));

        // Then
        assertThat(Objects.requireNonNull(accountService.getAccountInfo(new AccountDetails(account)).getBody())
                .getName()).isEqualTo(account.getName());
        assertThat(Objects.requireNonNull(accountService.getAccountInfo(new AccountDetails(account)).getBody())
                .getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    void 내_정보_수정() {
        // Given
        AccountDetails accountDetails = new AccountDetails(AccountFactory.createAccountA());
        ModifyAccountInfoRequestDto modifyAccountInfoRequestDto = AccountFactory.createModifyAccountInfoRequestDtoA();

        // When
        when(accountMapper.findByaccountId(accountDetails.getAccount().getAccountId()))
                .thenReturn(Optional.of(accountDetails.getAccount()));
        ResponseEntity<AccountInfoDto> responseEntity = accountService.modifyAccount(accountDetails, modifyAccountInfoRequestDto);

        // Then
        assertNotNull(responseEntity);
        assertThat(responseEntity.getBody().getContact()).isEqualTo(modifyAccountInfoRequestDto.getContact());
        assertThat(responseEntity.getBody().getBirthdate()).isEqualTo(modifyAccountInfoRequestDto.getBirthdate());
    }
}

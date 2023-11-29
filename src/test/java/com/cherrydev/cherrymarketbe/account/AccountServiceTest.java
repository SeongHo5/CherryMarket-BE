package com.cherrydev.cherrymarketbe.account;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.account.service.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.account.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.auth.service.AuthServiceImpl;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.factory.AccountFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Objects;
import java.util.Optional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class AccountServiceTest {

    @InjectMocks
    AccountServiceImpl accountService;
    @InjectMocks
    AuthServiceImpl authService;
    @Mock
    AccountMapper accountRepository;


    @Test
    void 회원가입_이메일중복() {
        // Given
        SignUpRequestDto signUpRequestDto = AccountFactory.createSignUpRequestDtoA();

        // When
        when(accountRepository.existByEmail(signUpRequestDto.getEmail())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> accountService.createAccount(signUpRequestDto))
                .isInstanceOf(DuplicatedException.class)
                .satisfies(e -> assertThat(DuplicatedException.class.cast(e).getStatusCode()).
                        isEqualTo(CONFLICT_ACCOUNT.getStatusCode()))
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
                "test1@example.com",
                "Testuser12#"
        );

        // When
        when(accountRepository.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.empty());

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
                "test1@example.com",
                "Testuser12#"
        );

        Account account = AccountFactory.createAccountA();

        // When
        when(accountRepository.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(account));

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
                "test1@example.com",
                "Testuser12#"
        );
        Account account = AccountFactory.createAccountB();

        // When
        when(accountRepository.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(account));

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
        when(accountRepository.findByaccountId(account.getAccountId())).thenReturn(Optional.of(account));

        // Then
        assertThat(Objects.requireNonNull(accountService.getAccountInfo(new AccountDetails(account)).getBody())
                .getName()).isEqualTo(account.getName());
        assertThat(Objects.requireNonNull(accountService.getAccountInfo(new AccountDetails(account)).getBody())
                .getEmail()).isEqualTo(account.getEmail());
    }

}

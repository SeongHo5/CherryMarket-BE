package com.cherrydev.cherrymarketbe.common.jwt;

import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.BLACK_LIST_KEY_PREFIX;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.BLACKLISTED_TOKEN;
import static com.cherrydev.cherrymarketbe.common.log.CherryLogger.logRequestInfo;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logRequestInfo(request);
        String userToken = jwtProvider.resolveToken(request);

        // 토큰이 존재할 때만 검증 - 인증을 요구하지 않는 API에 대한 처리
        if (userToken != null) {
            checkIfTokenInvalidated(userToken);
            jwtProvider.validateToken(userToken);

            Claims userInfo = jwtProvider.getInfoFromToken(userToken);
            setAuthentication(userInfo.getSubject());
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtProvider.createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkIfTokenInvalidated(String token) {
        if (redisService.hasKey(BLACK_LIST_KEY_PREFIX + token)) {
            throw new AuthException(BLACKLISTED_TOKEN);
        }
    }
}

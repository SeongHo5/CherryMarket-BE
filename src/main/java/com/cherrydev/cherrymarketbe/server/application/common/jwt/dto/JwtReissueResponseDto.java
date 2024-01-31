package com.cherrydev.cherrymarketbe.server.application.common.jwt.dto;


import lombok.Builder;

@Builder
public record JwtReissueResponseDto(String accessToken, String refreshToken, Long accessTokenExpiresIn) {

}

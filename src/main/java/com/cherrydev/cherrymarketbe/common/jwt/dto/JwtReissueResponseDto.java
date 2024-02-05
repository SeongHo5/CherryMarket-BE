package com.cherrydev.cherrymarketbe.common.jwt.dto;


import lombok.Builder;

@Builder
public record JwtReissueResponseDto(String accessToken, String refreshToken, Long accessTokenExpiresIn) {

}

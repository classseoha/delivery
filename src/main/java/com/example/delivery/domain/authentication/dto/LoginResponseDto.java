package com.example.delivery.domain.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto { // 로그인 성공 시 서버가 클라이언트에게 토큰 내려주는 DTO

    private final String accessToken;
    private final String refreshToken;

}

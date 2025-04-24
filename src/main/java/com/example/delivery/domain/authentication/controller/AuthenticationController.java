package com.example.delivery.domain.authentication.controller;

import com.example.delivery.domain.authentication.JwtTokenProvider;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/delivery/auth")
@RequiredArgsConstructor // final 필드들 자동 생성자 주입
public class AuthenticationController { // 로그인, 로그아웃 요청 처리 컨트롤러

    private final JwtTokenProvider jwtTokenProvider; // JWT를 생성하거나 검증하는 유틸 클래스
    private final UserService userService; // 이메일로 사용자를 조회하거나 비밀번호 체크하는 비즈니스 로직 담당
    private final RedisTemplate<String, String> redisTemplate; // Redis에 토큰을 저장, 조회하는 도구 (로그아웃 시 사용)
    private final UserRepository userRepository;

}

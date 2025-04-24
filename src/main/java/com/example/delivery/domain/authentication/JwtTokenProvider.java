package com.example.delivery.domain.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private final long tokenValidTime; // 토큰 유효시간 1시간 (밀리초 기준)

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.token-valid-time}") long tokenValidTime) {
        this.secretKey = secretKey;
        this.tokenValidTime = tokenValidTime;
    }

    // 이메일을 JWT로 변환해주는 메서드 (Header.Payload.Signature)
    public String createToken(String email) {

        Date now = new Date(); // 현재시간

        return Jwts.builder()
                .setSubject(email) // JWT payload에 들어갈 내용
                .setIssuedAt(now) // 발급 시간
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // 암호화 방식(HS256) + 비밀키
                .compact(); // JWT 문자열로 변환 (subject >> 토큰이 어떤 유저에 대한 것인지 식별하는 값)
    }
}

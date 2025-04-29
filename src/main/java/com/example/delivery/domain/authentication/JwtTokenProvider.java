package com.example.delivery.domain.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private final long accessTokenValidTime; // 토큰 유효시간 1시간 (밀리초 기준)
    private final long refreshTokenValidTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-valid-time}") long accessTokenValidTime, // 1시간
            @Value("${jwt.refresh-token-valid-time}") long refreshTokenValidTime // 2주
    ) {
        this.secretKey = secretKey;
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    // ✅ Access Token 생성
    public String createAccessToken(Long userId) {

        return createToken(userId, accessTokenValidTime);
    }

    // ✅ Refresh Token 생성
    public String createRefreshToken(Long userId) {

        return createToken(userId, refreshTokenValidTime);
    }

    // 이메일을 JWT로 변환해주는 메서드 (Header.Payload.Signature)
    public String createToken(Long userId, long validTime) {

        Date now = new Date(); // 현재시간

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // JWT payload에 들어갈 내용
                .setIssuedAt(now) // 발급 시간
                .setExpiration(new Date(now.getTime() + validTime)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // 암호화 방식(HS256) + 비밀키
                .compact(); // JWT 문자열로 변환 (subject >> 토큰이 어떤 유저에 대한 것인지 식별하는 값)
    }

    // 토큰에서 사용자 id값 추출 (Payload의 sub 값)
    public Long getUserId(String token) {

        String subject = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject); // string → long 변환
    }

    // 토큰 유효성 검증 (예외를 던지도록 변경)
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException ex) {
            log.warn("잘못된 JWT 서명입니다: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.warn("만료된 JWT 토큰입니다: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("지원되지 않는 JWT 토큰입니다: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT 토큰이 비었습니다: {}", ex.getMessage());
        }
        return false;
    }

    // 로그아웃을 위한 토큰 만료시간 계산 메서드
    public long getExpiration(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().getTime() - System.currentTimeMillis(); // 토큰 만료 시간에서 현재 시간을 뺌 >> 남은 시간(ms)
    }

    // 클라이언트 요청에서 JWT 토큰을 꺼내는 핵심 로직
    public String resolveToken(HttpServletRequest request) {

        // 클라이언트가 서버에 요청 보낼 때, JWT 토큰은 보통 HTTP 헤더에 담김 >> "Authorization(헤더): Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." 변수에 bearer ey... 문자열 담김
        String bearer = request.getHeader("Authorization");

        // 헤더가 아예 없는 요청일 수 있으니 null이면 건너뛰고, "Bearer "로 시작하는지 확인
        if (bearer != null && bearer.startsWith("Bearer")) {
            return bearer.substring(7).trim(); // "Bearer "는 총 7글자 → 앞의 "Bearer "를 잘라내고 토큰만 반환
        }

        return null; // Authorization 헤더가 없거나, "Bearer "로 시작하지 않으면 null 반환
    }
}

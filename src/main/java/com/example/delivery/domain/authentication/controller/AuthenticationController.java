package com.example.delivery.domain.authentication.controller;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.authentication.JwtTokenProvider;
import com.example.delivery.domain.authentication.dto.request.LoginRequestDto;
import com.example.delivery.domain.authentication.dto.response.LoginResponseDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/delivery/auth")
@RequiredArgsConstructor // final 필드들 자동 생성자 주입
public class AuthenticationController { // 로그인, 로그아웃 요청 처리 컨트롤러

    private final JwtTokenProvider jwtTokenProvider; // JWT를 생성하거나 검증하는 유틸 클래스
    private final UserService userService; // 이메일로 사용자를 조회하거나 비밀번호 체크하는 비즈니스 로직 담당
    private final RedisTemplate<String, String> redisTemplate; // Redis에 토큰을 저장, 조회하는 도구 (로그아웃 시 사용)
    private final UserRepository userRepository;

    // 로그인 성공 시 토큰을 응답으로 줘서 클라이언트가 저장함, 이후 요청부터는 이 토큰을 Authorization 헤더에 담아서 전송
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        String email = request.getEmail();

        // 이메일 형식 검증
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "이메일 형식이 잘못되었습니다.");
        }

        // 사용자 조회
        User user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 입력된 비밀번호와 DB에 저장된 암호화된 비밀번호를 비교
        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_CORRECT_VALUE, "비밀번호가 일치하지 않습니다.");
        }

        Long userId = user.getId();
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        // Redis에 해당 이메일로 로그인된 기록이 있다면 예외 처리
        String refreshKey = "refresh:userId:" + userId;
        redisTemplate.opsForValue().set(refreshKey, refreshToken, jwtTokenProvider.getExpiration(refreshToken), TimeUnit.MILLISECONDS);

        return ResponseEntity.ok(new LoginResponseDto(accessToken, refreshToken)); // 클라이언트에게 응답으로 전달 >> 이후 이 토큰을 요청 헤더에 담아야 인증 됨
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request); // Authorization 헤더에서 토큰을 꺼냄 (resolveToken)

        // 토큰 존재 여부 확인
        if (token == null) {
            throw new CustomException(ErrorCode.NON_EXISTENT_TOKEN, "토큰이 존재하지 않습니다.");
        }

        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtTokenProvider.getUserId(token); // ID 추출
        String loginKey = "login:userId:" + userId;

        // Redis에 로그인 상태가 있는 경우 제거
        if (redisTemplate.hasKey(loginKey)) {
            redisTemplate.delete(loginKey);
        }

        // Redis에 로그아웃 처리
        long expiration = jwtTokenProvider.getExpiration(token); // 토큰 만료 시간
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS); // 블랙리스트 등록

        // Refresh Token도 제거
        String refreshKey = "refresh:userId:" + userId;
        redisTemplate.delete(refreshKey);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.LOGOUT_SUCCESS, null, request.getRequestURI()));
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<LoginResponseDto> reissue(HttpServletRequest request) {

        // 클라이언트에서 보내온 Refresh Token
        String refreshToken = request.getHeader("Refresh-Token");

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "유효하지 않은 리프레시 토큰입니다.");
        }

        // 토큰에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserId(refreshToken);
        String refreshKey = "refresh:userId:" + userId;

        // Redis에서 저장된 Refresh Token 조회
        String storedRefreshToken = redisTemplate.opsForValue().get(refreshKey);

        // Redis에 없거나, 보낸 refresh token과 일치하지 않는 경우 예외 처리
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "저장된 리프레시 토큰과 일치하지 않습니다.");
        }

        // 새 Access Token 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);

        return ResponseEntity.ok(new LoginResponseDto(newAccessToken, refreshToken));
    }
}

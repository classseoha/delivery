package com.example.delivery.domain.authentication.filter;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.authentication.JwtAuthenticationException;
import com.example.delivery.domain.authentication.JwtTokenProvider;
import com.example.delivery.domain.authentication.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 요청마다 JWT 토큰이 있으면 확인하고, 로그인 상태로 만들어주는 역할(JWT 인증을 처리하는 핵심 보안 필터 클래스 >> 유저 정보를 SecurityContext에 넣어줌)
// 인증 정보를 SecurityContext에 등록해서 컨트롤러(@AuthenticationPrincipal 등)에서 사용자 정보를 쓸 수 있게 함
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter >> 한 요청 당 한 번만 실행하는 필터

    // JWT 검증 도구와 유저 정보를 불러올 도구를 주입
    private final JwtTokenProvider jwtTokenProvider; // 토큰 추출, 파싱, 검증, 사용자 ID 추출 기능을 담은 클래스
    private final UserDetailsService userDetailsService; // 사용자 정보를 UserDetails 형태로 가져오는 서비스
    private final RedisTemplate<String, String> redisTemplate; // Redis 접근용 → 로그아웃된 토큰인지 확인

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, RedisTemplate<String, String> redisTemplate) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException { // 특정 경로에서는 이 필터를 아예 건너뛰도록 설정

        String path = request.getRequestURI();
        return path.equals("/delivery/auth/login") || path.equals("/delivery/users/signup") || path.equals("/delivery/auth/token/reissue");
    }

    // 필터의 핵심 로직: 요청이 들어올 때마다 토큰을 꺼내서 검증하고 유저 인증을 넣어줌
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request); // 요청 헤더에서 "Authorization: Bearer <token>" 형식의 문자열에서 token만 꺼냄

        try {
        // 토큰이 null이거나 이상한 형식이면 그냥 다음 필터로 넘김
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

            // 로그아웃된 토큰인지 확인 (해당 키가 존재하면 이 토큰은 무효 처리해야 하니까 예외 발생)
            if (redisTemplate.hasKey(token)) {
                throw new JwtAuthenticationException("로그아웃 된 토큰입니다.");
            }

            // 유효성 검사 (JWT의 서명, 만료일, 포맷 등이 올바른지 확인)
            if (!jwtTokenProvider.validateToken(token)) {
                throw new JwtAuthenticationException("유효하지 않은 토큰입니다.");
            }

            // 토큰에서 이메일로 유저 정보를 찾아서 Authentication 객체를 생성 (서명 검증, 만료 체크 등 토큰이 올바른지 검사)
            Long userId = jwtTokenProvider.getUserId(token); // 토큰에서 id 꺼냄
            UserDetails userDetails = ((CustomUserDetailsService) userDetailsService).loadUserById(userId); // 토큰에서 꺼낸 userId로 해당 사용자 정보 가져옴

            // SecurityContext에 인증 객체 넣기 (비밀번호는 이미 토큰으로 인증되었기 때문에 null로 설정, 권한은 그대로 >> Spring Security에게 이 요청은 이 유저가 한거야 라고 알려주는 부분)
            // Spring Security에서 현재 요청이 "인증된 사용자"로 처리되게 만드는 핵심 구문
            // 이걸 등록하지 않으면 컨트롤러에서 @AuthenticationPrincipal 같은 걸로 유저 정보 못 씀
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 다음 필터로 넘기기 (인증을 마치고 나면 필터 체인을 다음으로 넘김)
            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException e) { // 발생한 예외를 request에 CustomException으로 담아 두고 이후 CustomAuthenticationEntryPoint가 이 값을 읽어서 JSON 에러 응답을 보낼 수 있게 함

            request.setAttribute("exception", new CustomException(ErrorCode.UNAUTHORIZED, e.getMessage()));
            throw new AuthenticationException("CustomException 발생") {};
        }
    }
}

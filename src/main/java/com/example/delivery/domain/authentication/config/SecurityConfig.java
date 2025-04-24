package com.example.delivery.domain.authentication.config;

import com.example.delivery.domain.authentication.JwtTokenProvider;
import com.example.delivery.domain.authentication.filter.CustomAccessDeniedHandler;
import com.example.delivery.domain.authentication.filter.CustomAuthenticationEntryPoint;
import com.example.delivery.domain.authentication.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// Spring Security의 전반적인 보안 설정을 담당하는 클래스 (요청에서 JWT 꺼내고, 유효한지 확인하고, 유저 정보를 SecurityContext에 등록하고, Redis에서 로그아웃 토큰인지 검사함)
// JWT 필터도 여기에 넣어서 인증을 커스터마이징 함
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity // Spring Security 기능을 사용하겠다고 선언
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider; // 토큰 생성, 파싱, 검증 담당 클래스
    private final UserDetailsService userDetailsService; // 이메일로 유저 정보 불러오는 인터페이스 구현체
    private final RedisTemplate<String, String> redisTemplate; // 로그아웃한 토큰을 Redis에 저장하거나 조회할 때 사용
    private final PasswordEncoder passwordEncoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;  // AccessDeniedHandler 주입

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, RedisTemplate<String, String> redisTemplate, PasswordEncoder passwordEncoder, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    // SecurityFilterChain → Spring Security에서 필터 체인을 정의하는 방식
    // HttpSecurity를 통해 HTTP 요청 보안 설정을 하나하나 해줄 수 있음
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호는 주로 세션 기반 인증에서 사용하는데, JWT 기반 인증에서는 불필요하기 때문에 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT는 세션을 사용하지 않는 인증 방식이기 때문에, STATELESS 설정을 통해 세션을 아예 사용하지 않음
                .authorizeHttpRequests(auth -> auth // URL 별 인증 설정
                        .requestMatchers(HttpMethod.PATCH, "/delivery").authenticated()
                        .requestMatchers(HttpMethod.POST, "/delivery/signup").permitAll()
                        .requestMatchers(HttpMethod.POST,"/delivery/auth/login").permitAll() // /authentication/** 로 시작하는 요청은 인증 없이 허용 (로그인/회원가입 등)
                        .requestMatchers(HttpMethod.POST,"/delivery/auth/logout").permitAll() // /authentication/** 로 시작하는 요청은 인증 없이 허용 (로그인/회원가입 등)

                        // 💡 오직 점주만 접근 가능한 API
                        .requestMatchers("/delivery/stores/**", "/delivery/store/**").hasRole("OWNER")

                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 로그인x 일 경우 발동
                        .accessDeniedHandler(customAccessDeniedHandler) // 로그인o, 권한x 일 경우 발동
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, redisTemplate), // JwtAuthenticationFilter 를 직접 만든 JWT 인증 필터로 등록
                        UsernamePasswordAuthenticationFilter.class // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 넣어서 기존 로그인 방식보다 먼저 동작하게 함
                );

        return http.build(); // 위에서 설정한 내용을 기반으로 SecurityFilterChain을 완성해서 반환함
    }
}

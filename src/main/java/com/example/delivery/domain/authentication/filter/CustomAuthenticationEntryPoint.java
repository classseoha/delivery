package com.example.delivery.domain.authentication.filter;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint { // Spring Security에서 인증 실패시(JWT 없거나 잘못됨) 호출됨 > 기본적으로 Spring Security는 인증 실패하면 HTML 에러 페이지 반환함

//    private final ObjectMapper objectMapper;
//
//    public CustomAuthenticationEntryPoint() {
//        this.objectMapper = new ObjectMapper();
//        this.objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 문제 해결
//    }

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        // 커스텀 필터에서 토큰 검증 중에 발생한 CustomException이 있을 수도 있어서 한 번 체크
        Exception exception = (Exception) request.getAttribute("exception");

        ApiResponseDto<Object> apiResponse;

        // CustomException이 있으면 그 에러코드에 맞게 응답을 만들고, 해당 HTTP 상태 코드를 세팅
        if (exception instanceof CustomException customException) {
            apiResponse = ApiResponseDto.fail(customException.getErrorCode(), request.getRequestURI());
            response.setStatus(customException.getHttpStatus().value());
        } else { // 그 외에는 기본 401 Unauthorized 처리
            // 기본 Unauthorized 처리
            apiResponse = ApiResponseDto.fail(ErrorCode.UNAUTHORIZED, request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

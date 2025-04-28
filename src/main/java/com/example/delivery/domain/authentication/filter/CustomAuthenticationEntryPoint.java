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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 문제 해결
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        // CustomException이 저장되어 있을 수 있으니 체크
        Exception exception = (Exception) request.getAttribute("exception");

        ApiResponseDto<Object> apiResponse;

        if (exception instanceof CustomException customException) {
            apiResponse = ApiResponseDto.fail(customException.getErrorCode(), request.getRequestURI());
            response.setStatus(customException.getHttpStatus().value());
        } else {
            // 기본 Unauthorized 처리
            apiResponse = ApiResponseDto.fail(ErrorCode.UNAUTHORIZED, request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

//        // 인증 실패 시 ApiResponseDto 포맷으로 응답
//        ApiResponseDto<Object> apiResponse = ApiResponseDto.fail(
//                ErrorCode.UNAUTHORIZED,  // ErrorCode에 따라 변경 가능
//                request.getRequestURI()  // 요청 경로 전달
//        );
//
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        // JSON으로 변환하여 응답
//        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
//        response.getWriter().write(jsonResponse);
    }
}

package com.example.delivery.domain.authentication.filter;

import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 권한 부족 시 ApiResponseDto 포맷으로 응답
        ApiResponseDto<Object> apiResponse = ApiResponseDto.fail(
                ErrorCode.NO_PERMISSION,  // ErrorCode에 따라 변경 가능
                request.getRequestURI()  // 요청 경로 전달
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON으로 변환하여 응답
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
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
public class CustomAccessDeniedHandler implements AccessDeniedHandler { // 권한 관련 발생하는 예외(403 Forbidden) 처리하는 커스텀 핸들러 > 로그인 했지만 접근 권한 없을 때

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Spring Security가 인가 실패(AccessDeniedException)를 감지하면 이 메서드를 실행
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 권한 부족 시 ApiResponseDto 포맷으로 응답
        ApiResponseDto<Object> apiResponse = ApiResponseDto.fail(
                ErrorCode.NO_PERMISSION,  // ErrorCode에 따라 변경 가능
                request.getRequestURI()  // 요청 경로 포함 (어디서 에러가 났는지 정보 제공)
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON으로 변환하여 응답
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
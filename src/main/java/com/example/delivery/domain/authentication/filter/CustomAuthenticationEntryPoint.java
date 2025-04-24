package com.example.delivery.domain.authentication.filter;

import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.response.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException)
            throws IOException, ServletException {

        // 인증 실패 시 ApiResponseDto 포맷으로 응답
        ApiResponseDto<Object> apiResponse = ApiResponseDto.fail(
                ErrorCode.UNAUTHORIZED,  // ErrorCode에 따라 변경 가능
                request.getRequestURI()  // 요청 경로 전달
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(apiResponse.toString());  // ApiResponseDto를 JSON 형식으로 반환
    }
}

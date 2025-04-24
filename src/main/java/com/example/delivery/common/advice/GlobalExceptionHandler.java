package com.example.delivery.common.advice;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.response.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // 400 Bad Request 관련 예외 통합 처리
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HandlerMethodValidationException.class,
            MissingRequestHeaderException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiResponseDto<?>> handleBadRequestExceptions(Exception e, HttpServletRequest request) {
        log.error("400 Bad Request: {} - {}", e.getClass().getSimpleName(), e.getMessage());

        String message = "잘못된 요청입니다.";
        if (e instanceof MethodArgumentNotValidException ex) {
            message = ex.getBindingResult().getFieldErrors().stream()
                    .findFirst()
                    .map(error -> error.getDefaultMessage())
                    .orElse(ErrorCode.BAD_REQUEST.getMessage());
        } else if (e instanceof MissingRequestHeaderException) {
            message = ErrorCode.MISSING_PARAMETER.getMessage();
        } else if (e instanceof MissingServletRequestParameterException) {
            message = ErrorCode.INVALID_PARAMETER.getMessage();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.fail(ErrorCode.BAD_REQUEST.getCode(), message, request.getRequestURI()));
    }

    // 존재하지 않는 URL 접근 시
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleNoPageFoundException(Exception e, HttpServletRequest request) {
        log.error("404 Not Found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDto.fail(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage(), request.getRequestURI()));
    }

    // 지원하지 않는 HTTP 메서드 요청 시
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDto<?>> handleMethodNotSupportedException(Exception e, HttpServletRequest request) {
        log.error("405 Method Not Allowed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponseDto.fail(ErrorCode.METHOD_NOT_ALLOWED.getCode(), ErrorCode.METHOD_NOT_ALLOWED.getMessage(), request.getRequestURI()));
    }

    // CustomException 처리 (비즈니스 로직 예외)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto<?>> handleCustomException(CustomException e, HttpServletRequest request) {
        log.error("CustomException: {}", e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponseDto.fail(e.getErrorCode().getCode(), e.getMessage(), request.getRequestURI()));
    }

    // 그 외 예기치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<?>> handleAllUnhandledException(Exception e, HttpServletRequest request) {
        log.error("500 Internal Server Error: {} - {}", e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), request.getRequestURI()));
    }
}
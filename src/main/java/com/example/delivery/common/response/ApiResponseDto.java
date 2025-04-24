package com.example.delivery.common.response;

import com.example.delivery.common.enums.ErrorCode;
import com.example.delivery.common.enums.SuccessCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiResponseDto<T> {

    private final LocalDateTime timestamp;
    private final Integer code;
    private final T data;
    private final String message;

    //성공응답 생성자
    public ApiResponseDto(SuccessCode successCode, T data) {
        this.timestamp = LocalDateTime.now();
        this.code = successCode.getCode();
        this.data = data;
        this.message = successCode.getMessage();
    }

    //실패응답 생성자
    public ApiResponseDto(ErrorCode errorCode, T data) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getCode();
        this.data = null;
        this.message = errorCode.getMessage();
    }

    //성공 응답 팩토리 메서드
    public static <T> ApiResponseDto<T> success(SuccessCode successCode, T data) {

        ApiResponseDto<T> successResponse = new ApiResponseDto<>(successCode, data);

        return successResponse;
    }


    public static <T> ApiResponseDto<List<T>> successList(SuccessCode successCode, List<T> data) {

        ApiResponseDto<List<T>> listApiResponseDto = new ApiResponseDto<>(successCode, data);

        return listApiResponseDto;

    }

    //실패 응답 팩토리 메서드
    public static <T> ApiResponseDto<T> fail(LocalDateTime timestamp, ErrorCode errorCode, T data) {
        ApiResponseDto<T> failureResponseDto = new ApiResponseDto<>(timestamp, errorCode, data);

        return failureResponseDto;
    }
}

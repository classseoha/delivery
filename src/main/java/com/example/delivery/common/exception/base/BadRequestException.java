package com.example.delivery.common.exception.base;

import com.example.delivery.common.exception.enums.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}

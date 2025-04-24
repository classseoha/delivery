package com.example.delivery.common.exception.base;

import com.example.delivery.common.exception.enums.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

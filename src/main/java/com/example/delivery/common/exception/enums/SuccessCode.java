package com.example.delivery.common.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {
    CREATE_SUCCESS(200, HttpStatus.CREATED,"가게를 오픈하였습니다."),
    GET_SUCCESS(200, HttpStatus.OK, "가게를 조회하였습니다."),
    GET_ONE_SUCCESS(200, HttpStatus.OK, "단일 가게를 조회하였습니다."),
    DELETE_SUCCESS(200, HttpStatus.OK, "가게 폐업완료하였습니다."),


    //메뉴
    CREATE_MENU_SUCCESS(200, HttpStatus.CREATED,"메뉴를 생성 하였습니다."),
    GET_MENU_SUCCESS(200, HttpStatus.OK, "메뉴 목록을 조회하였습니다."),
    GET_ONE_MENU_SUCCESS(200, HttpStatus.OK, "단일 메뉴를 조회하였습니다."),
    UPDATE_MENU_SUCCESS(200, HttpStatus.OK, "단일 메뉴를 수정하였습니다."),
    DELETE_MENU_SUCCESS(200, HttpStatus.OK, "메뉴 중지 완료하였습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    SuccessCode(Integer code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
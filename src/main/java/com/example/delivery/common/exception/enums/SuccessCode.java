package com.example.delivery.common.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {
    USER_CREATE_SUCCESS(200, HttpStatus.CREATED,"회원 가입이 완료되었습니다."),
    USER_UPDATE_SUCCESS(200, HttpStatus.OK,"회원 정보가 수정되었습니다."),
    USER_DELETE_SUCCESS(200, HttpStatus.OK,"회원 탈퇴가 완료되었습니다."),
    LOGOUT_SUCCESS(200, HttpStatus.OK,"로그아웃 되었습니다."),

    CREATE_SUCCESS(200, HttpStatus.CREATED,"가게를 오픈하였습니다."),
    GET_SUCCESS(200, HttpStatus.OK, "가게를 조회하였습니다."),
    DELETE_SUCCESS(200, HttpStatus.OK, "가게 폐업완료하였습니다."),
    PUT_SUCCESS(200, HttpStatus.OK, "가게 정보 수정을 완료하였습니다."),

    //주문
    CREATE_ORDER_SUCCESS(200, HttpStatus.CREATED, "주문을 완료하였습니다."),
    ADD_ITEM_TO_CART_SUCCESS(200, HttpStatus.CREATED, "장바구니에 메뉴와 수량이 정상적으로 들어갔습니다."),
    ORDER_STATUS_CHANGED(200, HttpStatus.OK, "주문이 상태가 정상적으로 변경되었습니다."),
    ORDER_REJECTED(200, HttpStatus.OK, "주문이 거절되었습니다."),

    //메뉴
    CREATE_MENU_SUCCESS(200, HttpStatus.CREATED,"메뉴를 생성 하였습니다."),
    GET_MENU_SUCCESS(200, HttpStatus.OK, "메뉴 목록을 조회하였습니다."),
    GET_ONE_MENU_SUCCESS(200, HttpStatus.OK, "단일 메뉴를 조회하였습니다."),
    UPDATE_MENU_SUCCESS(200, HttpStatus.OK, "단일 메뉴를 수정하였습니다."),
    DELETE_MENU_SUCCESS(200, HttpStatus.OK, "메뉴 상태 변경 완료하였습니다."),

    //리뷰
    CREATE_REVIEW(200,HttpStatus.CREATED,"리뷰가 생성되었습니다,"),
    GET_REVIEW_SUCESS(200,HttpStatus.OK,"리뷰가 조회되었습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    SuccessCode(Integer code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
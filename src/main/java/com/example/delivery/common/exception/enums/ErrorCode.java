package com.example.delivery.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 - 잘못된 요청
    BAD_REQUEST(400,HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_PARAMETER(400,HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    INVALID_PARAMETER(400,HttpStatus.BAD_REQUEST, "유효하지 않은 요청 파라미터입니다."),

    // 401
    UNAUTHORIZED(401,HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다."),
    USER_NOT_REGISTERED(401, HttpStatus.UNAUTHORIZED,  "회원가입을 완료한 후 로그인해주세요."),

    // 403 - 권한 없음
    NO_PERMISSION(403,HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 405 - 지원하지 않는 메서드
    METHOD_NOT_ALLOWED(405,HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드입니다."),


    // 404 - Not Found
    NOT_FOUND(404,HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    CART_NOT_FOUND(404, HttpStatus.NOT_FOUND, "카트 목록을 찾을 수 없습니다."),
    STORE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    MENU_NOT_FOUND(404, HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."),

    // 500 - 서버 내부 오류
    INTERNAL_SERVER_ERROR(500,HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    TEST_ERROR(500,HttpStatus.INTERNAL_SERVER_ERROR, "테스트 에러입니다."),

    // 주문
    ACCESS_DENIED_CART(403, HttpStatus.FORBIDDEN, "본인의 장바구니만 조회할 수 있습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}

package com.example.delivery.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //204
    NO_CONTENT(204,HttpStatus.NO_CONTENT,"조회된 리뷰가 없습니다"),
    // 400 - 잘못된 요청
    BAD_REQUEST(400,HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_PARAMETER(400,HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    INVALID_PARAMETER(400,HttpStatus.BAD_REQUEST, "유효하지 않은 요청 파라미터입니다."),
    EXISTED_PARAMETER(400,HttpStatus.BAD_REQUEST, "이미 존재하는 데이터입니다."),
    NOT_CORRECT_VALUE(400,HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    NO_VALUE_CHANGED(400,HttpStatus.BAD_REQUEST, "변경된 값이 없습니다."),
    ORDER_CANNOT_BE_REJECTED(400, HttpStatus.BAD_REQUEST, "이 주문은 거절할 수 없습니다."),
    INVALID_ORDER_STATUS(400, HttpStatus.BAD_REQUEST, "주문 상태를 변경 할수 없는 상태입니다."),
    REVIEW_NOT_ALLOWED(400, HttpStatus.BAD_REQUEST, "배달 완료된 주문만 리뷰 작성이 가능합니다."),
    STORE_NOT_OPEN(400, HttpStatus.BAD_REQUEST, "가게 오픈 시간에만 가능합니다."),
    INVALID_RATING_RANGE(400,HttpStatus.BAD_REQUEST,"최소 평점은 최대 평점보다 클 수 없습니다."),
    // 401
    UNAUTHORIZED(401,HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다."),
    USER_NOT_REGISTERED(401, HttpStatus.UNAUTHORIZED,  "회원가입을 완료한 후 로그인해주세요."),
    LOGGED_OUT_TOKEN(401, HttpStatus.UNAUTHORIZED,  "로그아웃 된 토큰입니다."),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED,  "유효하지 않은 토큰입니다."),
    NON_EXISTENT_TOKEN(401, HttpStatus.UNAUTHORIZED,  "토큰이 존재하지 않습니다."),
    ALREADY_DELETED_USER(401, HttpStatus.UNAUTHORIZED,  "이미 탈퇴한 사용자입니다."),


    // 403 - 권한 없음
    NO_PERMISSION(403,HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ACCESS_DENIED_CART(403, HttpStatus.FORBIDDEN, "본인의 장바구니만 조회할 수 있습니다."),
    ALREADY_ORDERED(403, HttpStatus.FORBIDDEN, "이미 주문된 장바구니 입니다" ),
    CART_ALREADY_EXISTS(403, HttpStatus.FORBIDDEN, "장바구니가 이미 존재합니다."),

    // 405 - 지원하지 않는 메서드
    METHOD_NOT_ALLOWED(405,HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드입니다."),


    // 404 - Not Found
    NOT_FOUND(404,HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    CART_NOT_FOUND(404, HttpStatus.NOT_FOUND, "카트 목록을 찾을 수 없습니다."),
    STORE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    MENU_NOT_FOUND(404, HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),

    // 500 - 서버 내부 오류
    INTERNAL_SERVER_ERROR(500,HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    TEST_ERROR(500,HttpStatus.INTERNAL_SERVER_ERROR, "테스트 에러입니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}

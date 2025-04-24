package com.example.delivery.domain.order.controller;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.order.dto.Response.CartDetailResponseDto;
import com.example.delivery.domain.order.dto.Response.CartsResponseDto;
import com.example.delivery.domain.order.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * 전체 장바구니 목록
     *
     * @param userId 회원 ID
     * @param httpServletRequest HttpServletRequest (요청 URI 정보)
     * @return 회원의 전체 장바구니 목록
     */
    @GetMapping("/cart")
    public ResponseEntity<ApiResponseDto<List<CartsResponseDto>>> getMyCartSummary(
            @RequestParam Long userId,
            HttpServletRequest httpServletRequest) {

        List<CartsResponseDto> cartSummary = cartService.getMyCarts(userId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_SUCCESS, cartSummary, httpServletRequest.getRequestURI()));
    }

    /**
     * 특정 장바구니 조회 API
     *
     * @param cartId   조회할 장바구니의 ID
     * @param userId   본인의 장바구니인지 확인하는 ID
     * @param httpServletRequest  HttpServletRequest (요청 URI 정보)
     * @return 해당 장바구니의 상세 정보
     * @throws CustomException CART_NOT_FOUND, NO_PERMISSION
     */
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<ApiResponseDto<CartDetailResponseDto>> getCartDetail(
            @PathVariable Long cartId,
            @RequestParam Long userId,
            HttpServletRequest httpServletRequest ) {

        CartDetailResponseDto cartDetail = cartService.getCartDetail(userId, cartId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_SUCCESS, cartDetail, httpServletRequest.getRequestURI()));
    }
}
package com.example.delivery.domain.order.controller;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.order.dto.Request.CartItemRequestDto;
import com.example.delivery.domain.order.dto.Response.CartDetailResponseDto;
import com.example.delivery.domain.order.dto.Response.CartsResponseDto;
import com.example.delivery.domain.order.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * @param httpServletRequest HttpServletRequest (요청 URI 정보)
     * @return 회원의 전체 장바구니 목록
     */
    @GetMapping("/cart")
    public ResponseEntity<ApiResponseDto<List<CartsResponseDto>>> getMyCartSummary(
            HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<CartsResponseDto> cartSummary = cartService.getMyCarts(userId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_SUCCESS, cartSummary, httpServletRequest.getRequestURI()));
    }

    /**
     * 특정 장바구니 조회 API
     *
     * @param cartId   조회할 장바구니의 ID
     * @param httpServletRequest  HttpServletRequest (요청 URI 정보)
     * @return 해당 장바구니의 상세 정보
     * @throws CustomException CART_NOT_FOUND, NO_PERMISSION
     */
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<ApiResponseDto<CartDetailResponseDto>> getCartDetail(
            @PathVariable Long cartId,
            HttpServletRequest httpServletRequest ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        CartDetailResponseDto cartDetail = cartService.getCartDetail(userId, cartId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_SUCCESS, cartDetail, httpServletRequest.getRequestURI()));
    }

    /**
     * 장바구니에 메뉴 담기 API
     *
     * @param storeId       가게 ID (어떤 가게에 메뉴를 담을지)
     * @param requestDto    메뉴 담기 요청 DTO (menuId, quantity)
     * @param httpServletRequest HttpServletRequest (요청 URI 정보)
     * @return 장바구니에 메뉴를 추가한 후의 결과
     * @throws CustomException USER_NOT_FOUND, STORE_NOT_FOUND, MENU_NOT_FOUND, INVALID_PARAMETER
     */
    @PostMapping("/cart/{storeId}/items")
    public ResponseEntity<ApiResponseDto<Void>> updateItemToCart(
            @PathVariable Long storeId,
            @RequestBody CartItemRequestDto requestDto,
            HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        cartService.updateItemToCart(userId, storeId, requestDto);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.ADD_ITEM_TO_CART_SUCCESS, null, httpServletRequest.getRequestURI()));
    }
}
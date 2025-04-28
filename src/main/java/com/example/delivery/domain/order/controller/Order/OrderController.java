package com.example.delivery.domain.order.controller.Order;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.order.dto.Response.Order.OrderDetailResponseDto;
import com.example.delivery.domain.order.dto.Response.Order.OrderResponseDto;
import com.example.delivery.domain.order.service.Order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 목록 조회 API
     *
     * @param httpServletRequest HttpServletRequest (요청 URI 정보)
     * @return 회원의 주문 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getMyOrders(
            HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<OrderResponseDto> orderSummary = orderService.getMyOrders(userId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_ORDER_SUCCESS, orderSummary, httpServletRequest.getRequestURI()));
    }

    /**
     * 주문 상세 조회 API
     *
     * @param orderId 조회할 주문의 ID
     * @param httpServletRequest HttpServletRequest (요청 URI 정보)
     * @return 해당 주문의 상세 정보
     * @throws CustomException ORDER_NOT_FOUND, NO_PERMISSION
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseDto<OrderDetailResponseDto>> getOrderDetail(
            @PathVariable Long orderId,
            HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        OrderDetailResponseDto orderDetail = orderService.getOrderDetail(userId, orderId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_ORDER_SUCCESS, orderDetail, httpServletRequest.getRequestURI()));
    }

    /**
     * 카트를 주문으로 생성하는 API
     *
     * @param cartId 주문할 카트 ID
     * @param httpServletRequest HttpServletRequest (요청 URI 정보)
     * @return 주문 생성 결과
     * @throws CustomException CART_NOT_FOUND, ALREADY_ORDERED, NO_PERMISSION
     */
    @PostMapping("/{cartId}")
    public ResponseEntity<ApiResponseDto<Void>> createOrder(
            @PathVariable Long cartId,
            HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        orderService.createOrder(userId, cartId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.CREATE_ORDER_SUCCESS, null, httpServletRequest.getRequestURI()));
    }
}
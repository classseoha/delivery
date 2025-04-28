package com.example.delivery.domain.order.controller.Order;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.order.dto.Response.Order.OrderStatusResponseDto;
import com.example.delivery.domain.order.service.Order.StoreOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/store/order")
@RequiredArgsConstructor
public class StoreOrderController {

    private final StoreOrderService orderService;

    /**
     * 주문 상태 변경 API
     *
     * @param orderId 주문 ID
     * @param storeId 가게 ID (사장님의 가게인지 확인)
     * @param httpServletRequest 요청 URI 정보 포함
     * @return 상태가 변경된 주문 ID와 변경된 상태를 포함한 DTO
     * @throws CustomException ORDER_NOT_FOUND, NO_PERMISSION, INVALID_ORDER_STATUS
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseDto<OrderStatusResponseDto>> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Long storeId,
            HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        OrderStatusResponseDto responseDto = orderService.changeOrderStatus(userId, orderId, storeId);

        return ResponseEntity.ok(ApiResponseDto.success(
                SuccessCode.ORDER_STATUS_CHANGED,
                responseDto,
                httpServletRequest.getRequestURI()
        ));
    }

    /**
     * 주문 거절 API
     *
     * @param orderId 주문 ID
     * @param storeId 가게 ID
     * @param httpServletRequest 요청 URI 정보
     * @return 거절된 주문 ID와 상태(REJECTED)를 포함한 DTO
     * @throws CustomException ORDER_NOT_FOUND, NO_PERMISSION, ORDER_CANNOT_BE_REJECTED
     */
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<ApiResponseDto<OrderStatusResponseDto>> rejectOrder(
            @PathVariable Long orderId,
            @RequestParam Long storeId,
            HttpServletRequest httpServletRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        OrderStatusResponseDto responseDto = orderService.rejectOrder(userId, orderId, storeId);

        return ResponseEntity.ok(ApiResponseDto.success(
                SuccessCode.ORDER_REJECTED,
                responseDto,
                httpServletRequest.getRequestURI()
        ));
    }
}

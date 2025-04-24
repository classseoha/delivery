package com.example.delivery.domain.order.service;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.Response.CartDetailResponseDto;
import com.example.delivery.domain.order.dto.Response.CartsResponseDto;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import com.example.delivery.domain.order.repository.CartItemRepository;
import com.example.delivery.domain.order.repository.CartRepository;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.common.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    // 유저가 가지고 있는 전체 장바구니 목록 조회
    @Transactional(readOnly = true)
    public List<CartsResponseDto> getMyCarts(Long userId) {
        List<Cart> carts = cartRepository.findAllByUser(userId);

        return carts.stream()
                .map(cart -> {
                    List<CartItem> cartItems = cartItemRepository.findByCart(cart);
                    return new CartsResponseDto(cart, cartItems);
                })
                .toList();
    }

    // 특정 카트의 상세 정보 조회
    @Transactional(readOnly = true)
    public CartDetailResponseDto getCartDetail(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        if (!cart.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        return new CartDetailResponseDto(cart, cartItems);
    }
}
package com.example.delivery.domain.order.service;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.Request.CartItemRequestDto;
import com.example.delivery.domain.order.dto.Response.CartDetailResponseDto;
import com.example.delivery.domain.order.dto.Response.CartsResponseDto;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import com.example.delivery.domain.order.repository.CartItemRepository;
import com.example.delivery.domain.order.repository.CartRepository;
import com.example.delivery.domain.store.entity.Store;
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
        // userId로 User 엔티티를 조회해야 함
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 해당 유저가 가진 장바구니 목록 조회
        List<Cart> carts = cartRepository.findAllByUser(user);

        return carts.stream()
                .map(eachCart -> {
                    List<CartItem> cartItems = cartItemRepository.findByCart(eachCart);
                    return new CartsResponseDto(eachCart, cartItems);
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

        return CartDetailResponseDto.from(cart, cartItems);
    }

    // 장바구니 추가 및 삭제
    public void updateItemToCart(Long userId, Long storeId, CartItemRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Cart cart = cartRepository.findByUserAndStore(user, store)
                .orElseGet(() -> cartRepository.save(new Cart(user, store)));

        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        // 장바구나 +, - 수량 계산 및 수량이 0일 경우 삭제
        CartItem existingItem = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menu.getId());
        if (existingItem != null) {
            existingItem.increaseQuantity(requestDto.getQuantity());
            if (existingItem.getQuantity() <= 0) {
                cartItemRepository.delete(existingItem);
            }
        } else {
            CartItem cartItem = new CartItem(cart, menu, requestDto.getQuantity());
            cartItemRepository.save(cartItem);
        }
    }
}
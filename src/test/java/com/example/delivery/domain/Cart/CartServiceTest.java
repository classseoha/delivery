package com.example.delivery.domain.Cart;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.Request.CartItemRequestDto;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import com.example.delivery.domain.order.repository.CartItemRepository;
import com.example.delivery.domain.order.repository.CartRepository;
import com.example.delivery.domain.order.service.Cart.CartService;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testAddMultipleItemsToCart() {
        // Arrange
        Long userId = 1L;
        Long storeId = 2L;
        CartItemRequestDto requestDto1 = new CartItemRequestDto(); // 첫 번째 메뉴
        CartItemRequestDto requestDto2 = new CartItemRequestDto(); // 두 번째 메뉴
        CartItemRequestDto requestDto3 = new CartItemRequestDto(); // 세 번째 메뉴

        ReflectionTestUtils.setField(requestDto1, "menuId", 1L);
        ReflectionTestUtils.setField(requestDto1, "quantity", 2);

        ReflectionTestUtils.setField(requestDto2, "menuId", 2L);
        ReflectionTestUtils.setField(requestDto2, "quantity", 3);

        ReflectionTestUtils.setField(requestDto3, "menuId", 1L);
        ReflectionTestUtils.setField(requestDto3, "quantity", -1);


        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Menu menu1 = new Menu(); // mock 첫 번째 메뉴
        Menu menu2 = new Menu(); // mock 두 번째 메뉴
        Menu menu3 = new Menu(); // mock 세 번째 메뉴

        // mock 첫 번째 메뉴
        ReflectionTestUtils.setField(menu1, "id", 1L);
        ReflectionTestUtils.setField(menu1, "menuName", "testMenu1");
        ReflectionTestUtils.setField(menu1, "price", 1000);

        // mock 두 번째 메뉴
        ReflectionTestUtils.setField(menu2, "id", 2L);
        ReflectionTestUtils.setField(menu2, "menuName", "testMenu2");
        ReflectionTestUtils.setField(menu2, "price", 1500);

        // mock 세 번째 메뉴
        ReflectionTestUtils.setField(menu3, "id", 1L);
        ReflectionTestUtils.setField(menu3, "menuName", "testMenu1");
        ReflectionTestUtils.setField(menu3, "price", 1000);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(menuRepository.findById(requestDto1.getMenuId())).thenReturn(Optional.of(menu1));
        when(menuRepository.findById(requestDto2.getMenuId())).thenReturn(Optional.of(menu2));
        when(menuRepository.findById(requestDto3.getMenuId())).thenReturn(Optional.of(menu3));

        Cart cart = new Cart(user, store); // mock cart
        when(cartRepository.findByUserAndStore(user, store)).thenReturn(Optional.of(cart));

        // Act
        cartService.updateItemToCart(userId, storeId, requestDto1); // 첫 번째 아이템 추가
        cartService.updateItemToCart(userId, storeId, requestDto2); // 두 번째 아이템 추가
        cartService.updateItemToCart(userId, storeId, requestDto3); // 세 번째 아이템 추가

        // Assert
        verify(cartItemRepository, times(3)).save(any(CartItem.class)); // 세 번 호출되었는지 확인
    }

}

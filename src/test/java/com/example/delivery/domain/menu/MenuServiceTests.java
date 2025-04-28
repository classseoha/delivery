package com.example.delivery.domain.menu;

import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.base.UnauthorizedException;
import com.example.delivery.domain.menu.dto.request.MenuRequestDto;
import com.example.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.delivery.domain.menu.dto.response.MenuResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.entity.MenuStatus;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.entity.UserAuthority;
import com.example.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MenuServiceTests {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("메뉴 저장 테스트")
    @Test
    void 성공() {
        Long userId = 1L;
        Long storeId = 1L;
        MenuRequestDto requestDto = new MenuRequestDto(storeId, "마라탕", "맵기 조절 가능", 12000);

        User user = new User();
        ReflectionTestUtils.setField(user, "userAuthority", UserAuthority.OWNER);
        Store store = new Store();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuResponseDto response = menuService.save(userId, storeId, requestDto);

        assertThat(response.getMenuName()).isEqualTo("마라탕");
    }

    @Test
    void 유저를_찾을_수_없으면_NotFoundException() {
        Long userId = 1L;
        Long storeId = 1L;
        MenuRequestDto requestDto = new MenuRequestDto(storeId, "마라탕", "맵기 조절 가능", 12000);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> menuService.save(userId, storeId, requestDto));
    }


    @DisplayName("메뉴 수정 테스트")
    @Test
    void 수정성공() {
        Long userId = 1L;
        Long menuId = 1L;
        Store store = new Store();
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(store, "user", user);

        Menu menu = new Menu(store, "마라탕", "맵기 조절 가능", 12000);
        MenuUpdateRequestDto updateDto = new MenuUpdateRequestDto("우육탕", "덜 맵게", 15000);

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        MenuResponseDto response = menuService.update(userId, menuId, updateDto);

        assertThat(response.getMenuName()).isEqualTo("우육탕");
    }

    @Test
    void 다른_유저가_수정시도_UnauthorizedException() {
        Long userId = 1L;
        Long menuId = 1L;
        Store store = new Store();
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 2L);
        ReflectionTestUtils.setField(store, "user", user);

        Menu menu = new Menu(store, "마라탕", "맵기 조절 가능", 12000);
        MenuUpdateRequestDto updateDto = new MenuUpdateRequestDto("우육탕", "덜 맵게", 15000);

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        assertThrows(UnauthorizedException.class, () -> menuService.update(userId, menuId, updateDto));
    }


    @DisplayName("메뉴 삭제 테스트")
    @Test
    void 삭제성공() {
        Long userId = 1L;
        Long menuId = 1L;
        Store store = new Store();
        User user = mock(User.class);
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(store, "user", user);
        ReflectionTestUtils.setField(user, "userAuthority", UserAuthority.OWNER);

        Menu menu = new Menu(store, "마라탕", "맵기 조절 가능", 12000);

        when(user.getId()).thenReturn(userId);
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        menuService.changeStatusMenu(userId, menuId);

        assertThat(menu.getMenuStatus()).isEqualTo(MenuStatus.DELETED); // 상태 변경 확인
    }

    @Test
    void 다른_유저가_삭제시도시_UnauthorizedException() {
        Long userId = 1L;
        Long menuId = 1L;
        Store store = new Store();
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 2L);
        ReflectionTestUtils.setField(store, "user", user);


        Menu menu = new Menu(store, "마라탕", "맵기 조절 가능", 12000);

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        assertThrows(UnauthorizedException.class, () -> menuService.changeStatusMenu(userId, menuId));
    }


    @DisplayName("메뉴 조회 테스트")
    @Test
    void 단건조회_성공() {
        Long menuId = 1L;
        Long storeId = 1L;
        Store store = new Store();
        Menu menu = new Menu(store, "마라탕", "맵기 조절 가능", 12000);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(menuRepository.findByIdAndMenuStatus(menuId, MenuStatus.ACTIVE)).thenReturn(Optional.of(menu));

        MenuResponseDto response = menuService.findOne(menuId, storeId);

        assertThat(response.getMenuName()).isEqualTo("마라탕");
    }

    @Test
    void 메뉴_없으면_NotFoundException() {
        Long menuId = 1L;
        Long storeId = 1L;

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(new Store()));
        when(menuRepository.findByIdAndMenuStatus(menuId, MenuStatus.ACTIVE)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> menuService.findOne(menuId, storeId));
    }
}


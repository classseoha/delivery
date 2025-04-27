package com.example.delivery.domain.menu.service;

import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.base.UnauthorizedException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.menu.dto.request.MenuRequestDto;
import com.example.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.delivery.domain.menu.dto.response.MenuResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.entity.MenuStatus;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.entity.UserAuthority;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    /**
     *
     * @param storeId
     * @param menuRequestDto
     * @return
     */
    @Transactional
    public MenuResponseDto save(Long userId, Long storeId, MenuRequestDto menuRequestDto){
        User user = userRepository.findById(userId)
                            .orElseThrow(()-> new NotFoundException(ErrorCode.MENU_NOT_FOUND));
        Store store = storeRepository.findById(storeId)
                            .orElseThrow(()-> new NotFoundException(ErrorCode.MENU_NOT_FOUND));
        Menu menu = new Menu(store, menuRequestDto.getMenuName(),
                             menuRequestDto.getIntro(), menuRequestDto.getPrice());

        // 유저가 OWNER일 경우에만 생성 가능.
        if(user.getUserAuthority().equals(UserAuthority.OWNER)){
            menuRepository.save(menu);
        }

        return new MenuResponseDto(menu.getId(), store.getId(), menu.getMenuName(),
                                    menu.getIntro(), menu.getPrice());
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> findByStore(Long storeId){
        List<Menu> menus = menuRepository.findByStoreIdAndMenuStatus(storeId, MenuStatus.ACTIVE)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));


        return menus.stream()
                .map(menu -> new MenuResponseDto(
                        menu.getId(),
                        menu.getStore().getId(),
                        menu.getMenuName(),
                        menu.getIntro(),
                        menu.getPrice()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuResponseDto findOne(Long menuId, @RequestParam Long storeId) {

        Store store = storeRepository.findById(storeId)
                        .orElseThrow(()-> new NotFoundException(ErrorCode.MENU_NOT_FOUND));
        Menu menu = menuRepository.findByIdAndMenuStatus(menuId, MenuStatus.ACTIVE)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));

        return MenuResponseDto.builder()
                        .id(menu.getId())
                        .storeId(storeId)
                        .menuName(menu.getMenuName())
                        .intro(menu.getIntro())
                        .price(menu.getPrice())
                        .build();

    }

    @Transactional
    public MenuResponseDto update(Long userId, Long menuId, MenuUpdateRequestDto menuUpdateRequestDto) {
        Menu menu = menuRepository.findById(menuId)
                                .orElseThrow(()-> new NotFoundException(ErrorCode.MENU_NOT_FOUND));

        // 본인 가게가 아닐 경우
        if(!menu.getStore().getUser().getId().equals(userId)){
            throw new UnauthorizedException("메뉴 수정 권한이 없습니다.");
        }
        menu.update(menuUpdateRequestDto);

        return new MenuResponseDto(menu.getId(), menu.getStore().getId(), menu.getMenuName(),
                menu.getIntro(), menu.getPrice());

    }

    @Transactional
    public void delete(Long userId, Long menuId) {

        Menu menu = menuRepository.findById(menuId)
                            .orElseThrow(()-> new NotFoundException(ErrorCode.MENU_NOT_FOUND));

        // 본인 가게가 아닐 경우
        if(!menu.getStore().getUser().getId().equals(userId)){
            throw new UnauthorizedException("메뉴 삭제 권한이 없습니다.");
        }

        //메뉴 상태 전환
        menu.changeStatus();

    }
}

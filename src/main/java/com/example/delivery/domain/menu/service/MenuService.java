package com.example.delivery.domain.menu.service;

import com.example.delivery.common.exception.base.NotFoundException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto save(Long storeId, MenuRequestDto menuRequestDto){
        User user = userRepository.findById(menuRequestDto.getUserId())
                            .orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND));
        Store store = storeRepository.findById(storeId)
                            .orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND));
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
        List<Menu> menus = menuRepository.findByStoreId(storeId)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

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
    public MenuResponseDto findOne(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        return new MenuResponseDto(menu.getId(), menu.getStore().getId(), menu.getMenuName(),
                                menu.getIntro(), menu.getPrice());

    }


    public MenuResponseDto update(Long userId, Long menuId, MenuUpdateRequestDto menuUpdateRequestDto) {
        Menu menu = menuRepository.findById(menuId)
                                .orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND));

        // 본인 가게일 경우
        if(menu.getStore().getUser().equals(userId)){
            menu.update(menuUpdateRequestDto);
        }

        return new MenuResponseDto(menu.getId(), menu.getStore().getId(), menu.getMenuName(),
                menu.getIntro(), menu.getPrice());
        
    }

    public void delete(Long menuId) {

        Menu menu = menuRepository.findById(menuId)
                            .orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND));

        // 삭제되지 않았을 경우
        if(!menu.getMenuStatus() == MenuStatus.DELETED){
            menu.change
        }
    }
}

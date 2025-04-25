package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.CreateResponseDto;
import com.example.delivery.domain.store.dto.GetStoreResponseDto;
import com.example.delivery.domain.store.dto.UpdateResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.entity.UserAuthority;
import com.example.delivery.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override //가게 오픈
    public CreateResponseDto createStore(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount) {

        // Spring Security가 관리하는 인증 정보(Authentication)를 가져오고 인증정보에서 현재 로그인한 사용자 이름을 꺼냄
        //시큐리티 적용사항이니 모든 컨트롤러 메서드 내부에 적용시키시면 됩니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        //시큐리티 로부터 로그인한 유저 정보를 꺼냄
        //userId 는 시큐리티 config내에서  권한을 다 정해서 왔기때문에 여기서 인가를 할 필요가 없음
        User findUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));


        //유저가게의 개수를 세기위해
        Long count = storeRepository.countStoresByUser(findUser);
        if (count > 4) {
            throw new IllegalArgumentException("가게는 최대 3개까지 오픈할수 있습니다.");
        }


        // 중복된 가게 이름이 있으면 예외처리 하였습니다
        storeRepository.findStoreByStoreName(storeName).ifPresent(store -> {
                    throw new IllegalArgumentException("중복된 가게 이름입니다.");
        });

        Store store = new Store(storeName, openingTime, closingTime, minAmount,findUser);

        Store savedStore = storeRepository.save(store);

        return new CreateResponseDto(savedStore.getId()
                , savedStore.getStoreName()
                , savedStore.getOpeningTime()
                , savedStore.getClosingTime()
                , savedStore.getMinAmount()
                , savedStore.getStoreStatus());
    }

    @Transactional
    @Override // 가게 전체 조회
    public List<GetStoreResponseDto> findAll() {


        List<Store> findAllStoreList = storeRepository.findAll();

        List<GetStoreResponseDto> responseDtoList = new ArrayList<>();
        for (Store stores : findAllStoreList) {

            GetStoreResponseDto getStoreResponseDto = new GetStoreResponseDto(stores.getId()
                    , stores.getStoreName()
                    , stores.getOpeningTime()
                    , stores.getClosingTime()
                    , stores.getMinAmount()
                    , stores.getStoreStatus());

            responseDtoList.add(getStoreResponseDto);
        }

        return responseDtoList;
    }

    @Transactional
    @Override
    public UpdateResponseDto updateStore(Long id, String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        //가게 아이디가져오고
        Store findStore = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("가게가 없습니다."));

        // 유저 가지고 오고
        User findUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        //각각의 아이디를 불러와서 비교를한다.
        if (!findStore.getUser().getId().equals(findUser.getId())) {
            throw new IllegalArgumentException("본인의 가게만 수정할 수 있습니다.");
        }

        // 같으면 그 다음 로직을 실행한다.
        Store updateStore = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("수정할 가게가 없습니다."));

        updateStore.update(storeName, openingTime, closingTime, minAmount);

        return new UpdateResponseDto(updateStore.getStoreName(),updateStore.getOpeningTime(),updateStore.getClosingTime(),updateStore.getMinAmount(),updateStore.getStoreStatus());

    }

    @Transactional
    @Override
    public void deleteStore(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        //가게 아이디가져오고
        Store findStore = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("가게가 없습니다."));

        // 유저 가지고 오고
        User findUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        //각각의 아이디를 불러와서 비교를한다.

        if (!findStore.getUser().getId().equals(findUser.getId())) {
            throw new IllegalArgumentException("본인의 가게만 수정할 수 있습니다.");
        }

        storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("삭제할 가게가 없습니다."));

        storeRepository.deleteById(id);
    }
}

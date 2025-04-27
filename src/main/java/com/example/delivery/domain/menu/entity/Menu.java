package com.example.delivery.domain.menu.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.menu.dto.request.MenuRequestDto;
import com.example.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name ="status")
    private MenuStatus menuStatus = MenuStatus.ACTIVE;

    public Menu(Store store, String menuName, String intro, Integer price) {
        this.store = store;
        this.menuName = menuName;
        this.intro = intro;
        this.price = price;
    }

    public void update(MenuUpdateRequestDto menuUpdateRequestDto) {
        this.menuName = menuUpdateRequestDto.getMenuName();
        this.intro = menuUpdateRequestDto.getIntro();
        this.price = menuUpdateRequestDto.getPrice();
    }

    public void changeStatus(){
        if(this.menuStatus == MenuStatus.ACTIVE){
            this.menuStatus = MenuStatus.DELETED;
        }else if(this.menuStatus == MenuStatus.DELETED){
            this.menuStatus = MenuStatus.ACTIVE;
        }else{
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
    }

    public void setStore(Store store) {
        
    }
}

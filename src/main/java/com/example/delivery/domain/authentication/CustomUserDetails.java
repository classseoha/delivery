package com.example.delivery.domain.authentication;

import com.example.delivery.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails { // Spring Security에서 인증 처리를 할 때 핵심적으로 사용되는 UserDetails 구현체 (유저 권한에 따라 ROLE_USER, ROLE_OWNER를 동적으로 부여할 수 있게 설계 / 추후 권한 추가 시 enum에만 추가하면 확장 쉬움)

    private final User user;

    public CustomUserDetails(User user) { // User 엔티티의 모든 것 포함 (로그인 시 DB에서 찾은 User 객체를 받아 UserDetails로 래핑하는 역할)

        this.user = user;
    }

    @Override // 이 유저가 어떤 권한을 가지고 있는가?
    public Collection<? extends GrantedAuthority> getAuthorities() { // 제네릭 와일드카드 문법(GrantedAuthority를 상속하거나 구현한 모든 클래스의 객체들로 구성된 컬렉션을 받겠다는 뜻 / ? extends를 쓰면 GrantedAuthority 또는 그 하위 타입도 받을 수 있음)
        // 예: USER → ROLE_USER, OWNER → ROLE_OWNER (UserAuthority.USER → ROLE_USER, UserAuthority.OWNER → ROLE_OWNER 자동 매핑됨)
        return Collections.singletonList( // 권한 1개만 있는 리스트 생성
                new SimpleGrantedAuthority("ROLE_" + user.getUserAuthority().name())
        );
    }

    @Override
    public String getPassword() { // 로그인 시 입력한 이메일/비밀번호가 DB의 정보와 일치하는지 비교하는 데 사용됨

        return user.getPassword();
    }

    @Override
    public String getUsername() { // 로그인 시 입력한 이메일/비밀번호가 DB의 정보와 일치하는지 비교하는 데 사용됨

        return String.valueOf(user.getId());
    }

    @Override
    public boolean isAccountNonExpired() { // 계정 만료x

        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠김x

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 만료x

        return true;
    }

    @Override
    public boolean isEnabled() {

        return user.isActive(); // 비활성 유저 로그인 못하게
    }

    public User getUser() { // 필요한 경우 원래 User 엔티티를 다시 꺼낼 수 있게 함 (ex. 로그인한 유저 정보를 컨트롤러에서 조회할 때)

        return user;
    }
}

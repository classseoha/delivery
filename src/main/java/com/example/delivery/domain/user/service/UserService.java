package com.example.delivery.domain.user.service;

import com.example.delivery.domain.user.dto.SignUpRequestDto;
import com.example.delivery.domain.user.dto.SignUpResponseDto;
import com.example.delivery.domain.user.dto.UpdateUserRequestDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder; >> SecurityConfig 생성 후 사용

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        if(requestDto.getEmail() == null || requestDto.getEmail().isBlank() || !requestDto.getEmail().contains("@")){
            throw new IllegalArgumentException("유효한 이메일을 입력해주세요.");
        }

        if(requestDto.getPassword() == null || requestDto.getPassword().isBlank()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

//        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getEmail(), encodedPassword, requestDto.getAddress(), requestDto.getUserAuthority());

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getEmail(), savedUser.getAddress(), savedUser.getUserAuthority(), savedUser.getModifiedAt());
    }

    @Transactional
    public void updateUser(String email, UpdateUserRequestDto requestDto) {

        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        boolean isUpdated = false;

        // 새 비밀번호 있을 경우 암호화 후 저장
        if (requestDto.getNewPassword() != null && !requestDto.getNewPassword().isBlank()) {
            // 현재 비밀번호와 새 비밀번호가 같다면 예외 처리
            if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
                throw new IllegalArgumentException("새 비밀번호가 기존 비밀번호와 같습니다.");
            }
            String encodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
            user.updatePassword(encodedPassword);
            isUpdated = true;
        }

        // 새 주소 있을 경우 저장
        if (requestDto.getNewAddress() != null && !requestDto.getNewAddress().isBlank()) {
            user.updateAddress(requestDto.getNewAddress());
            isUpdated = true;
        }

        // 아무것도 변경 안 했을 경우 예외 처리
        if (!isUpdated) {
            throw new IllegalArgumentException("변경할 정보가 없습니다.");
        }
    }

    @Transactional
    public void deleteUser(String email, String password) {

        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        User findUser = userRepository.findByIdOrElseThrow(email);

        // 기존 비밀번호와 입력한 비밀번호가 다른지 확인
        if(!passwordEncoder.matches(password, findUser.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // soft delete 처리
        user.deleteUser();
    }
}

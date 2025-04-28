package com.example.delivery.domain.user.service;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.user.dto.request.SignUpRequestDto;
import com.example.delivery.domain.user.dto.response.SignUpResponseDto;
import com.example.delivery.domain.user.dto.request.UpdateUserRequestDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        if(requestDto.getEmail() == null || requestDto.getEmail().isBlank() || !requestDto.getEmail().contains("@")){
            throw new CustomException(ErrorCode.INVALID_PARAMETER, "이메일 형식이 잘못되었습니다.");
        }

        if(requestDto.getPassword() == null || requestDto.getPassword().isBlank()){
            throw new CustomException(ErrorCode.MISSING_PARAMETER, "비밀번호가 누락되었습니다.");
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.EXISTED_PARAMETER, "이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getEmail(), encodedPassword, requestDto.getAddress(), requestDto.getUserAuthority());

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getEmail(), savedUser.getAddress(), savedUser.getUserAuthority(), savedUser.getModifiedAt());
    }

    @Transactional
    public void updateUser(Long userId, UpdateUserRequestDto requestDto) {

        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_CORRECT_VALUE, "비밀번호가 일치하지 않습니다.");
        }

        boolean isUpdated = false;

        // 새 비밀번호 있을 경우 암호화 후 저장
        if (requestDto.getNewPassword() != null && !requestDto.getNewPassword().isBlank()) {
            // 현재 비밀번호와 새 비밀번호가 같다면 예외 처리
            if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.NO_VALUE_CHANGED, "새 비밀번호가 기존 비밀번호와 같습니다.");
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
            throw new CustomException(ErrorCode.NO_VALUE_CHANGED, "변경할 정보가 없습니다.");
        }
    }

    @Transactional
    public void deleteUser(Long userId, String password) {

        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 탈퇴한 사용자인지 체크
        if (!user.isActive()) {
            throw new CustomException(ErrorCode.ALREADY_DELETED_USER, "이미 탈퇴한 사용자입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_CORRECT_VALUE, "비밀번호가 일치하지 않습니다.");
        }

        // soft delete 처리
        user.deleteUser();
    }

    // 비밀번호 검증
    public boolean checkPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

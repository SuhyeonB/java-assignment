package com.example.assignment.domain.user.service;

import com.example.assignment.domain.user.dto.request.LoginRequestDto;
import com.example.assignment.domain.user.dto.request.SignupRequestDto;
import com.example.assignment.domain.user.entity.User;
import com.example.assignment.domain.user.repository.UserRepository;
import com.example.assignment.domain.user.role.Role;
import com.example.assignment.global.exception.ApiErrorCode;
import com.example.assignment.global.exception.ApiException;
import com.example.assignment.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public record SignupResult(User user, String token) {}

    public SignupResult signup(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByUsername(signupRequestDto.getUsername())) {
            throw new ApiException(ApiErrorCode.USER_ALREADY_EXISTS, "이미 가입된 사용자입니다.");
        }

        Role role;

        if(!StringUtils.hasText(signupRequestDto.getRole())) {
            role = Role.USER;
        } else {
            try {
                role = Role.of(signupRequestDto.getRole());
            } catch (IllegalArgumentException e) {
                throw new ApiException(ApiErrorCode.INVALID_ROLE, "유효하지 않은 role입니다.");
            }
        }

        User user = new User(
                signupRequestDto.getUsername(),
                signupRequestDto.getPassword(),
                signupRequestDto.getNickname(),
                role);
        User savedUser = userRepository.save(user);

        String token =  jwtUtil.createToken(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
        return new SignupResult(savedUser, token);
    }

    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new ApiException(ApiErrorCode.NOT_FOUND_USER, "사용자를 찾을 수 없습니다."));

        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            throw new ApiException(ApiErrorCode.INVALID_CREDENTIALS, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole());
    }

    public User grantAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.NOT_FOUND_USER, "사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ADMIN) {
            user.grantAdmin();
            userRepository.save(user);
        }

        return user;
    }
}

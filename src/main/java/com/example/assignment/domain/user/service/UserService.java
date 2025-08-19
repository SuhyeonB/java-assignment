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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String signup(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByUsername(signupRequestDto.getUsername())) {
            throw new ApiException(ApiErrorCode.USER_ALREADY_EXISTS, "이미 가입된 사용자입니다.");
        }
        User user = new User(signupRequestDto.getUsername(), signupRequestDto.getPassword(), signupRequestDto.getNickname(), Role.USER);
        User savedUser = userRepository.save(user);

        return jwtUtil.createToken(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }

    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new ApiException(ApiErrorCode.NOT_FOUND_USER, "User not found"));

        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            throw new ApiException(ApiErrorCode.INVALID_CREDENTIALS, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole());
    }
}

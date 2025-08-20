package com.example.assignment.domain.user.controller;

import com.example.assignment.domain.user.dto.request.LoginRequestDto;
import com.example.assignment.domain.user.dto.request.SignupRequestDto;
import com.example.assignment.domain.user.dto.response.UserResponseDto;
import com.example.assignment.domain.user.dto.response.TokenResponseDto;
import com.example.assignment.domain.user.entity.User;
import com.example.assignment.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        var result = userService.signup(signupRequestDto);
        var body = UserResponseDto.from(result.user());
        return ResponseEntity.ok().header("Authorization", result.token()).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        String token = userService.login(loginRequestDto);
        return ResponseEntity.ok().header("Authorization", token).body(new TokenResponseDto(token));
    }

    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<UserResponseDto> grantAdmin(@PathVariable Long userId){
        User user = userService.grantAdmin(userId);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }
}

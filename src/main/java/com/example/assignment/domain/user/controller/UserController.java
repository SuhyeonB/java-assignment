package com.example.assignment.domain.user.controller;

import com.example.assignment.domain.user.dto.request.LoginRequestDto;
import com.example.assignment.domain.user.dto.request.SignupRequestDto;
import com.example.assignment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequestDto signupRequestDto) {
        String token = userService.signup(signupRequestDto);
        return ResponseEntity.ok().header("Authorization", token).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.login(loginRequestDto);
        return ResponseEntity.ok().header("Authorization", token).build();
    }
}

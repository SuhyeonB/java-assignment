package com.example.assignment.domain.user.controller;

import com.example.assignment.domain.user.dto.request.SignupRequestDto;
import com.example.assignment.domain.user.entity.User;
import com.example.assignment.domain.user.role.Role;
import com.example.assignment.domain.user.service.UserService;
import com.example.assignment.global.exception.ApiErrorCode;
import com.example.assignment.global.exception.ApiException;
import com.example.assignment.global.exception.GlobalExceptionHandler;
import com.example.assignment.global.security.JwtAuthenticationFilter;
import com.example.assignment.global.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({SecurityConfig.class, GlobalExceptionHandler.class,})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // given
        SignupRequestDto dto = new SignupRequestDto("JIN HO", "1234", "Mentos", null);
        User user = new User(1L, "JIN HO", "1234", "Mentos", Role.USER);
        UserService.SignupResult result = new UserService.SignupResult(user, "token-value");

        given(userService.signup(any())).willReturn(result);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "username": "JIN HO",
                              "password": "1234",
                              "nickname": "Mentos"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "token-value"))
                .andExpect(jsonPath("$.username").value("JIN HO"));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 사용자")
    void signup_fail_duplicate() throws Exception {
        // given
        given(userService.signup(any()))
                .willThrow(new ApiException(ApiErrorCode.USER_ALREADY_EXISTS, "이미 가입된 사용자입니다."));

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "username": "jinHo",
                              "password": "1234",
                              "nickname": "Mentos"
                            }
                            """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"));
    }


    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // given
        given(userService.login(any())).willReturn("login-jwt-token");

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"username":"abc","password":"pw"}
                        """))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "login-jwt-token"))
                .andExpect(jsonPath("$.token").value("login-jwt-token"));
    }

    @Test
    @DisplayName("로그인 실패")
    void login_invalid_credentials() throws Exception {
        // given
        given(userService.login(any()))
                .willThrow(new ApiException(
                        ApiErrorCode.INVALID_CREDENTIALS,
                        "아이디 또는 비밀번호가 올바르지 않습니다."));

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"username":"abc","password":"wrong"}
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
                .andExpect(jsonPath("$.error.message").exists());
    }

    @Test
    void grantAdmin() {
    }
}
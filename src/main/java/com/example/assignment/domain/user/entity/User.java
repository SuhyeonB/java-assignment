package com.example.assignment.domain.user.entity;

import com.example.assignment.domain.user.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class User {

    @Setter
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Role role;

    public User(String username, String password, String nickname, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public User(Long id, String username, String password, String nickname, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void changeNickname(String nickname) { this.nickname = nickname; }
    public void grantAdmin() { this.role = Role.ADMIN; }
}

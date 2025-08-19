package com.example.assignment.global.config;

import com.example.assignment.domain.user.repository.InMemoryUserRepository;
import com.example.assignment.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }
}

package com.example.assignment.domain.user.repository;

import com.example.assignment.domain.user.entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository{
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<String, Long> usernameIndex = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Long id = usernameIndex.get(username);
        return id == null ? Optional.empty() : Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean existsByUsername(String username) {
        return usernameIndex.containsKey(username);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) user.setId(seq.getAndIncrement());
        users.put(user.getId(), user);
        usernameIndex.put(user.getUsername(), user.getId());
        return user;
    }
}

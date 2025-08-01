package com.trumpecy.tictactoe.datasource.repository.user;

import com.trumpecy.tictactoe.domain.model.user.User;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByLogin(String login);

    void save(User newUser);

    Page<User> findAll(int page, int size);

    Optional<User> findById(UUID id);

    Page<User> findAllLoginContains(String search, int page, int size);
}

package com.trumpecy.tictactoe.datasource.repository.user;

import com.trumpecy.tictactoe.domain.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByLogin(String login);

    void save(User newUser);

    List<User> findAll();

    Optional<User> findById(UUID id);
}

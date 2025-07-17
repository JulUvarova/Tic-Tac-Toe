package com.school21.Tic_Tac_Toe.datasource.repository.user;

import com.school21.Tic_Tac_Toe.domain.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByLogin(String login);

    void save(User newUser);

    List<User> findAll();

    Optional<User> findById(UUID id);
}

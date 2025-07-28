package com.trumpecy.tictactoe.domain.service.user;

import com.trumpecy.tictactoe.domain.model.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();

    User getById(UUID id);

    User getByLogin(String login);

    void create(String login, String password);
}

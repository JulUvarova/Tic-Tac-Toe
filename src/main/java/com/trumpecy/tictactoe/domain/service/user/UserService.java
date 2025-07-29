package com.trumpecy.tictactoe.domain.service.user;

import com.trumpecy.tictactoe.domain.model.user.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    Page<User> getAllUsersPageable(int page, int size);

    User getById(UUID id);

    User getByLogin(String login);

    void create(String login, String password);
}

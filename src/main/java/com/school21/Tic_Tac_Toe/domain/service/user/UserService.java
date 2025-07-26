package com.school21.Tic_Tac_Toe.domain.service.user;

import com.school21.Tic_Tac_Toe.domain.model.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(UUID id);

    User getUserByLogin(String login);
}

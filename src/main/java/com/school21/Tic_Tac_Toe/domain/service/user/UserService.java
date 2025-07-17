package com.school21.Tic_Tac_Toe.domain.service.user;

import com.school21.Tic_Tac_Toe.domain.model.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UUID login(String username, String password);

    boolean register(String username, String password);

    List<User> getAllUsers();

    User getUserById(UUID id);
}

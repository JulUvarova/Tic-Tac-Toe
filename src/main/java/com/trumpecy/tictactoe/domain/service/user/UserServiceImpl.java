package com.trumpecy.tictactoe.domain.service.user;

import com.trumpecy.tictactoe.datasource.repository.user.UserRepository;
import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.exception.EntityNotFoundException;
import com.trumpecy.tictactoe.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Page<User> getAllUsersPageable(String search, int page, int size) {
        if (search == null || search.trim().isEmpty()) {
            return userRepository.findAll(page, size);
        }
        return userRepository.findAllLoginContains(search.trim(), page, size);
    }

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void create(String login, String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User %s already exists", login));
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        userRepository.save(newUser);
    }
}

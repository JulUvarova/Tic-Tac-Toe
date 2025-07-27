package com.school21.Tic_Tac_Toe.domain.service.user;

import com.school21.Tic_Tac_Toe.datasource.repository.user.UserRepository;
import com.school21.Tic_Tac_Toe.domain.model.user.User;
import com.school21.Tic_Tac_Toe.exception.EntityNotFoundException;
import com.school21.Tic_Tac_Toe.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
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

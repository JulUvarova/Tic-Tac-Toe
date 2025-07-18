package com.school21.Tic_Tac_Toe.domain.service.user;

import com.school21.Tic_Tac_Toe.datasource.repository.user.UserRepository;
import com.school21.Tic_Tac_Toe.domain.model.user.User;
import com.school21.Tic_Tac_Toe.exception.EntityNotFoundException;
import com.school21.Tic_Tac_Toe.exception.InvalidUserDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean register(String login, String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            return false;
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        return true;
    }

    @Override
    public UUID login(String login, String password) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new InvalidUserDataException("Invalid login"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserDataException("Invalid password");
        }
        return user.getId();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}

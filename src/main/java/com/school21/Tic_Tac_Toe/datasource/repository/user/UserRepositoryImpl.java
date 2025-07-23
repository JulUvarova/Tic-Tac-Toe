package com.school21.Tic_Tac_Toe.datasource.repository.user;

import com.school21.Tic_Tac_Toe.datasource.mapper.UserDataMapper;
import com.school21.Tic_Tac_Toe.datasource.model.UserEntity;
import com.school21.Tic_Tac_Toe.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userRepository;

    @Override
    public Optional<User> findByLogin(String login) {
        Optional<UserEntity> user = userRepository.findUserEntityByLogin(login);
        if (user.isPresent()) {
            return Optional.of(UserDataMapper.toModel(user.get()));
        }
        return Optional.empty();
    }

    @Override
    public void save(User newUser) {
        userRepository.save(UserDataMapper.toEntity(newUser));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "login")).stream().map(UserDataMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            return Optional.of(UserDataMapper.toModel(user.get()));
        }
        return Optional.empty();
    }
}

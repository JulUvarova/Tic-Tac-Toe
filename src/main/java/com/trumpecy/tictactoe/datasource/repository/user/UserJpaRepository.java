package com.trumpecy.tictactoe.datasource.repository.user;

import com.trumpecy.tictactoe.datasource.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserEntityByLogin(String login);
}

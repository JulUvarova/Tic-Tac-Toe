package com.school21.Tic_Tac_Toe.datasource.repository;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GameJpaRepository extends CrudRepository<GameEntity, UUID> {
}
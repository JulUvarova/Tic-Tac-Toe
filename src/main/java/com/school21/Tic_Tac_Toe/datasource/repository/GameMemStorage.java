package com.school21.Tic_Tac_Toe.datasource.repository;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import org.springframework.stereotype.Controller;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Controller
public class GameMemStorage {
    private final ConcurrentMap<UUID, GameEntity> storage = new ConcurrentHashMap<>();

    public void saveGame(UUID id, GameEntity game) {
        storage.put(id, game);
    }

    public GameEntity getGame(UUID id) {
        return storage.get(id);
    }
}

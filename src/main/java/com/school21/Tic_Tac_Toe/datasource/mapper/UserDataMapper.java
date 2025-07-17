package com.school21.Tic_Tac_Toe.datasource.mapper;

import com.school21.Tic_Tac_Toe.datasource.model.UserEntity;
import com.school21.Tic_Tac_Toe.domain.model.user.User;

public class UserDataMapper {
    public static UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setLogin(user.getLogin());
        entity.setPassword(user.getPassword());
        return entity;
    }

    public static User toModel(UserEntity entity) {
        if (entity == null) return null;

        return new User(entity.getId(), entity.getLogin(), entity.getPassword());
    }
}

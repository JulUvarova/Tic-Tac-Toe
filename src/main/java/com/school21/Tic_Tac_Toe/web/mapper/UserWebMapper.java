package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.user.User;
import com.school21.Tic_Tac_Toe.web.model.UserDto;

public class UserWebMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(user.getId(), user.getLogin());
    }

    public static User toModel(UserDto dto) {
        if (dto == null) return null;
        return new User(dto.getId(), dto.getLogin(), null);
    }
}

package com.trumpecy.tictactoe.web.mapper;

import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.web.model.user.UserDtoResponse;

public class UserWebMapper {
    public static UserDtoResponse toDto(User user) {
        if (user == null) return null;
        return new UserDtoResponse(user.getId(), user.getLogin(),
            user.getRoles() != null ? user.getRoles().stream().map(Enum::name).toList() : null);
    }
}

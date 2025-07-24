package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.user.User;
import com.school21.Tic_Tac_Toe.web.model.UserDtoResponse;

public class UserWebMapper {
    public static UserDtoResponse toDto(User user) {
        if (user == null) return null;
        return new UserDtoResponse(user.getId(), user.getLogin());
    }
}

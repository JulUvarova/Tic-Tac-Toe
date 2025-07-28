package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.token.Token;
import com.school21.Tic_Tac_Toe.web.model.token.JwtResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenWebMapper {
    public static JwtResponse toJwtResponse(Token token) {
        if (token == null) {
            return null;
        }
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setType(token.getType());
        jwtResponse.setAccessToken(token.getAccessToken());
        jwtResponse.setRefreshToken(token.getRefreshToken());

        return jwtResponse;
    }
}
